package timetogether.oauth2.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import timetogether.jwt.service.JwtService;
import timetogether.oauth2.CustomOAuth2User;
import timetogether.oauth2.entity.SocialType;
import timetogether.oauth2.repository.UserRepository;

import java.io.IOException;
import java.util.HashMap;
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

            // TODO : 동일한 사용자가 다른 브라우저에서 로그인할 경우 처리해보기
            loginSuccess(request, response, socialId); // 로그인에 성공한 경우 access, refresh 토큰 생성
        } catch (Exception e) {
            throw e;
        }
    }

    private String extractSocialId(CustomOAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes(); // 소셜 로그인 정보
        SocialType socialType = oAuth2User.getSocialType(); // 소셜 타입


        return switch (socialType) { // 소셜 타입에 해당하는 소셜 아이디 반환
            case NAVER -> getNaverSocialId(attributes);
            case GOOGLE -> getGoogleSocialId(attributes);
            case KAKAO -> getKakaoSocialId(attributes);
            default -> null; // 실패 예외처리 하기
        };
    }

    private String getNaverSocialId(Map<String, Object> attributes) {
        return (String) ((Map<String, Object>) attributes.get("response")).get("id");
    }

    private String getGoogleSocialId(Map<String, Object> attributes) {
        return (String) attributes.get("sub");
    }

    private String getKakaoSocialId(Map<String, Object> attributes) {
        return String.valueOf(attributes.get("id"));
    }


    // TODO : RefreshToken 유/무에 따라 다르게 처리해보기
    private void loginSuccess(HttpServletRequest request, HttpServletResponse response, String socialId) throws IOException {
        log.info("socialId = {}", socialId);
        String accessToken = jwtService.createAccessToken(socialId); // 사용자의 소셜 아이디로 액세스토큰을 생성
        String refreshToken = jwtService.createRefreshToken(); // 리프레시 토큰을 생성

        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken); // 헤더 설정
        jwtService.updateRefreshToken(socialId, refreshToken); // 사용자의(소셜아이디) 리프레시 토큰를 db에 저장
        //String redirectUrl = "http://172.20.10.4:3000/oauth2/redirect?access_token=" + accessToken;

        String userName = userRepository.findBySocialId(socialId).get().getUserName();
        //프론트랑 통신할 때 여기 설정
        String redirectUrl = "http://192.168.12.91:3000/login/oauth2/redirect?access_token=" + accessToken + "&refresh_token=" + refreshToken;//Front ip 로 설정
        response.sendRedirect(redirectUrl);

//        HttpSession session = request.getSession();
//        log.info("Session ID in loginSuccess: {}", session.getId());
//        session.setAttribute("accessToken", accessToken);
//        session.setAttribute("refreshToken", refreshToken);
//
//        String accessToken1 = (String) session.getAttribute("accessToken");
//        String refreshToken1 = (String) session.getAttribute("refreshToken");
//        log.info("redirect : accessToken : { }, refresh : { }", accessToken1, refreshToken1);
//
//        String redirectUrl = "http://192.168.233.242:3000/login/oauth2/redirect";
//        response.sendRedirect(redirectUrl);
    }
}
