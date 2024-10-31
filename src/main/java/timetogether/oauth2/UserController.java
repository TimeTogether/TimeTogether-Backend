package timetogether.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/jwt-test")
    public String jwtTest() { // JWT 서비스 테스트를 위한 API
        return "jwtTest 요청 성공";
    }
}
