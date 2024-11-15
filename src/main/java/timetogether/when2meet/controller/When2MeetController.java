package timetogether.when2meet.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import timetogether.global.response.BaseResponse;
import timetogether.global.response.BaseResponseService;
import timetogether.jwt.service.JwtService;
import timetogether.groupMeeting.MeetType;
import timetogether.when2meet.dto.*;
import timetogether.when2meet.service.When2MeetService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("/group/{groupId}/when")
@Controller
public class When2MeetController {

    private final When2MeetService when2MeetService;
    private final BaseResponseService baseResponseService;
    private final JwtService jwtService;

    @GetMapping
    public BaseResponse<Object> viewGroupMeetResult(@PathVariable("groupId") Long groupId){
        MeetTableDTO table = when2MeetService.viewMeetResult(groupId); // 최종 회의 일정에 대한 정보를 넘겨준다
        return baseResponseService.getSuccessResponse(table);
    }


    @PostMapping("/meet/add")
    public BaseResponse<Object> addGroupMeet(@RequestBody String groupMeetingTitle, @RequestBody List<String> dates, @PathVariable("groupId") Long groupId){
        when2MeetService.addGroupMeet(groupMeetingTitle, dates, groupId); // 회의를 하나 더 만든다 = group meeting table을 생성한다
        return baseResponseService.getSuccessResponse();
    }

    @GetMapping("/{title}/{type}")
    public BaseResponse<Object> viewGroupMeet(@PathVariable("groupId") Long groupId, @PathVariable("title") String groupMeetingTitle, @PathVariable("type") String type) // group table을 위한 정보
    {   // (그룹, 회의 제목, TYPE을 확인)
        GroupTableDTO<Users> table = when2MeetService.viewMeet(groupId, groupMeetingTitle, MeetType.fromString(type)); // 해당 type, group, meet에 대한 모든 사용자의 정보를 넘긴다
        return baseResponseService.getSuccessResponse(table);
    }

    @PostMapping("/{title}/{type}/add")
    public BaseResponse<Object> addUserMeet(HttpServletRequest request,
                                            @PathVariable("groupId") Long groupId,
                                            @PathVariable("title") String groupMeetingTitle,
                                            @PathVariable("type") String type)
    {   // (그룹, 회의 제목, TYPE을 확인) -> 회의 아이디로 테이블을 조회해서 해당 날짜에 대한 RankTime(off or on) 테이블 생성 및 저장
        String accessToken = jwtService.extractAccessToken(request).get();
        String socialId = jwtService.extractId(accessToken).get();
        GroupTableDTO<Days> table = when2MeetService.addUserMeet(groupId, groupMeetingTitle, MeetType.fromString(type), socialId);
        return baseResponseService.getSuccessResponse(table);
    }

    @PostMapping("/{type}/load")
    public BaseResponse<Object> loadUserMeet(HttpServletRequest request,
                                             @PathVariable("groupId") Long groupId,
                                             @PathVariable("type") String type)
    {   // (그룹, 회의 제목, TYPE을 확인) -> 회의 아이디로 테이블을 조회해서 해당 date에 대해 캘린더 일정을 확인 (추출하기)
        // date로 일정 정보를 가져와서, 시간을 확인해야한다 (시간은 localTime으로 비교한다)
        String accessToken = jwtService.extractAccessToken(request).get();
        String socialId = jwtService.extractId(accessToken).get();
        GroupTableDTO<Days> table = when2MeetService.loadUserMeet(groupId, MeetType.fromString(type), socialId);
        return baseResponseService.getSuccessResponse(table);
    }

    @PostMapping("/{title}/{type}/update")
    public BaseResponse<Object> updateUserMeet(HttpServletRequest request,
                                               @PathVariable("groupId") Long groupId,
                                               @PathVariable("type") String type,
                                               @PathVariable("title") String groupMeetingTitle,
                                               @RequestBody List<Days> days)
    {   // (그룹, 회의 제목, TYPE을 확인) -> 회의 아이디, Type, 사용자로 테이블을 조회해서 when2meet을 가져온 후 ranktime을 업데이트한다
        // ranktime을 set한다 (time, rank)
        String accessToken = jwtService.extractAccessToken(request).get();
        String socialId = jwtService.extractId(accessToken).get();
        when2MeetService.updateUserMeet(groupId, groupMeetingTitle, MeetType.fromString(type), socialId, days);
        return baseResponseService.getSuccessResponse();
    }

    @PostMapping("/{title}/{type}/done")
    public BaseResponse<Object> doneGroupMeet(HttpServletRequest request,
                                              @PathVariable("groupId") Long groupId,
                                              @PathVariable("title") String groupMeetingTitle,
                                              @PathVariable("type") String type,
                                              @RequestBody String meetDT)
    {   // (그룹, 회의 제목, TYPE을 확인) -> 해당 회의 아이디에 대해서 회의 일정을 생성한다
        // 사용자 아이디는 request에서 얻어오고, 캘린더 아이디를 접근한다
        // done을 누르는 순간 '일정 테이블'을 생성한다 (더이상 수정할 수 없음)

        String accessToken = jwtService.extractAccessToken(request).get();
        String socialId = jwtService.extractId(accessToken).get();
        when2MeetService.doneGroupMeet(groupId, groupMeetingTitle, MeetType.fromString(type), socialId, meetDT);
        return baseResponseService.getSuccessResponse();
    }


}
