package timetogether.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import timetogether.jwt.service.JwtService;

@Controller
@RequiredArgsConstructor
@Slf4j
public class NextController {

    private final JwtService jwtService;

    @GetMapping("/nextFunction")
    public ResponseEntity<?> nextFunction(HttpServletRequest request) {
        String accessToken = String.valueOf(jwtService.extractAccessToken(request));
        String refreshToken = String.valueOf(jwtService.extractRefreshToken(request));

        log.info("nextController:" + accessToken);

        // 토큰을 사용하여 다음 작업 수행
        if (jwtService.isTokenValid(accessToken) == true && jwtService.isTokenValid(refreshToken) == true) {
            // 유효성 검사 후 필요한 작업 수행
            return ResponseEntity.status(HttpStatus.OK).body("Token is valid");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is missing");
        }
    }

    @GetMapping("/home")
    @ResponseBody
    public String home(){
        return "ok";
    }

}
