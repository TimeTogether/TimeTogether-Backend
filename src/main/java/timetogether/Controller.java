package timetogether;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import timetogether.jwt.service.JwtService;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class Controller {

    @Autowired
    JwtService jwtService;
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
}
