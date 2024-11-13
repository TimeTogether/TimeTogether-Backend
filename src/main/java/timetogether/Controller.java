package timetogether;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import timetogether.jwt.service.JwtService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@AllArgsConstructor
@Slf4j
public class Controller {

    JwtService jwtService;
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 변환용 ObjectMapper

    @GetMapping("/header-info")
    @ResponseBody
    public String getHeaderInfo(@RequestHeader(value = "Authorization", defaultValue = "No Token") String authHeader) {
        // 헤더 값 출력
        return "Authorization Header: " + authHeader;
    }

    @GetMapping("/header")
    @ResponseBody
    public String getHeaderInfo(HttpServletRequest request) {
        // 헤더 값 출력
        Optional<String> accessToken = jwtService.extractAccessToken(request);
        log.info("accessToken = {}", accessToken.get());
        Optional<String> socialId = jwtService.extractId(accessToken.get());
        log.info("socialId = {}", socialId.get());

        return "hello";
    }

    @GetMapping("login/oauth2/redirect")
    public ResponseEntity<?> redirectHandler(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        log.info("Session ID in loginSuccess: {}", session.getId());
        String accessToken = (String) session.getAttribute("accessToken");
        String refreshToken = (String) session.getAttribute("refreshToken");
        log.info("redirect : accessToken : { }, refresh : { }", accessToken, refreshToken);

        if (accessToken != null && refreshToken != null) {
            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);

            // Map을 JSON으로 자동 변환하여 반환
            return ResponseEntity.ok().body(tokens);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 존재하지 않습니다.");
        }
    }


}
