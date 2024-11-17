package timetogether.mypage.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import timetogether.when2meet.dto.Result;

@Controller
@AllArgsConstructor
@RequestMapping("/user/history")
public class MypageController {

    @GetMapping
    public Result findMeetByUser(HttpServletRequest request){
        // 사용자와 연관되어있는 모든 과거 약속 정보를 조회해서 반환한다 (현재 시간 이전만 가져오기 - localDate 정보 사용)
        return null;
    }


    @GetMapping("{title}/search")
    public Result searchMeetByUser(HttpServletRequest request, @PathVariable("title") String title){
        // 사용자와 연관되어있는 모든 약속 정보 중 title에 해당하는 과거일정만 조회해서 반환한다
        return null;
    }

}
