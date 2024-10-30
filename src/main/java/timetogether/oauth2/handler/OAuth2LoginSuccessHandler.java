package timetogether.oauth2.handler;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import timetogether.jwt.service.JwtService;
import timetogether.oauth2.CustomOAuth2User;
import timetogether.oauth2.entity.Role;
import timetogether.oauth2.repository.UserRepository;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공!");
        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            String socialId = extractSocialId(oAuth2User);

            if (socialId == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "소셜 ID를 가져올 수 없습니다.");
                return;
            }

            // 처음 요청하든, 두번째 요청하든 로그인이 가능하도록
            loginSuccess(response, socialId); // 로그인에 성공한 경우 access, refresh 토큰 생성

        } catch (Exception e) {
            throw e;
        }
    }

    private String extractSocialId(CustomOAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String registrationId = oAuth2User.getName(); // OAuth2 제공자 (google, naver, kakao 등)

        log.info("registrationId={}, {}",registrationId, attributes);

        switch (registrationId) {
            case "google":
                return (String) attributes.get("sub"); // Google의 고유 ID

            case "kakao":
                return String.valueOf(attributes.get("id")); // Kakao의 고유 ID

            case "naver":
                Map<String, Object> response = (Map<String, Object>) attributes.get("response");
                return (String) response.get("id"); // Naver의 고유 ID

            default:
                return null; // 알 수 없는 제공자일 경우 null 반환
        }
    }


    // TODO : 소셜 로그인 시에도 무조건 토큰 생성하지 말고 JWT 인증 필터처럼 RefreshToken 유/무에 따라 다르게 처리해보기
    private void loginSuccess(HttpServletResponse response, String socialId) throws IOException {
        String accessToken = jwtService.createAccessToken(socialId); // 사용자의 소셜 아이디로 액세스토큰을 생성
        String refreshToken = jwtService.createRefreshToken(); // 리프레시 토큰을 생성
        response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken); // 헤더에 넣어요
        response.addHeader(jwtService.getRefreshHeader(), "Bearer " + refreshToken); // 헤더에 넣어요

        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken); // 보냅니다
        jwtService.updateRefreshToken(socialId, refreshToken); // 소셜 아이디와 리프레시 토큰으로 업데이트해요
    }


}
