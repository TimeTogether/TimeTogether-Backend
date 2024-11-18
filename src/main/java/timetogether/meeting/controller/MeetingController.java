package timetogether.meeting.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import timetogether.jwt.service.JwtService;
import timetogether.meeting.service.MeetingService;
import timetogether.when2meet.dto.GroupTableDTO;
import timetogether.when2meet.dto.Result;

@Controller
@AllArgsConstructor
@RequestMapping("/meet/list")
public class MeetingController {

    private final JwtService jwtService;
    private final MeetingService meetingService;

    @GetMapping("/")
    public Result getMeetByGroup(HttpServletRequest request){
        // request 정보로 사용자를 가져온다
        // 사용자가 묶여있는 그룹에 대한 정보를 넘겨준다 (그룹 아이디, 그룹 이름)
        String accessToken = jwtService.extractAccessToken(request).get();
        String socialId = jwtService.extractId(accessToken).get();
        meetingService.getMeetByGroup(socialId);
        return null;
    }

    @GetMapping("{groupId}/find")
    public Result findMeetByGroup(HttpServletRequest request, @PathVariable("groupId") Long groupId){
        // 그룹 아이디로 그룹을 가져온다
        // request 정보로 사용자를 가져온다
        // 그룹이름과 사용자로 캘린더로부터 그룹미팅 정보를 가져와서, 일정 제목만 반환한다

        // null일 경우 전체 예정된 회의 일정을 출력한다

        return null;
    }

    @GetMapping("{groupId}/{title}/search") // 회의 제목에 대해서만 검색이 가능하도록 한다
    public Result searchMeet(){
        // 그룹 아이디로 그룹을 가져온다
        // request 정보로 사용자를 가져온다
        // 그룹이름과 사용자로 캘린더로부터 그룹미팅 정보를 가져와서, 회의제목과 유사한 것들을 모두 반환한다

        return null;
    }



}
