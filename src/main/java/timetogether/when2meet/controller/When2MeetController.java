package timetogether.when2meet.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import timetogether.global.response.BaseResponse;
import timetogether.global.response.BaseResponseService;
import timetogether.jwt.service.JwtService;
import timetogether.groupMeeting.MeetType;
import timetogether.when2meet.dto.Days;
import timetogether.when2meet.dto.GroupTableDTO;
import timetogether.when2meet.dto.Result;
import timetogether.when2meet.dto.Users;
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
        List<Result> view = when2MeetService.viewMeetResult(groupId); // 최종 회의 일정에 대한 정보를 넘겨준다
        return baseResponseService.getSuccessResponse(view);
    }


    @PostMapping("/meet/add")
    public BaseResponse<Object> addGroupMeet(HttpServletRequest request, @RequestBody List<String> dates, @PathVariable("groupId") Long groupId){
        // (그룹, 회의 ID, date를 확인 -> 회의 아이디와 date로 meet과 when2meet 테이블 생성(off,on) 및 저장)
        Optional<String> accessToken = jwtService.extractAccessToken(request);
        Optional<String> socialId = jwtService.extractId(accessToken.get());
        when2MeetService.addGroupMeet(socialId.get(), dates, groupId); // 회의를 하나 더 만든다 = meet table을 생성한다
        return baseResponseService.getSuccessResponse();
    }

    @GetMapping("/{type}")
    public BaseResponse<Object> viewGroupMeet(@PathVariable("groupId") Long groupId, @PathVariable("type") String type) // group table을 위한 정보
    {   // (그룹, 회의 ID, TYPE을 확인)
        GroupTableDTO<Users> table = when2MeetService.viewMeet(groupId, MeetType.fromString(type)); // 해당 type, group, meet에 대한 모든 사용자의 정보를 넘긴다
        return baseResponseService.getSuccessResponse(table);
    }

    @PostMapping("/{type}/add")
    public BaseResponse<Object> addUserMeet(@PathVariable("groupId") Long groupId,
                                            @PathVariable("type") String type,
                                            @RequestBody String userNames)
    {   // (그룹, 회의 ID, TYPE을 확인) -> 회의 아이디로 테이블을 조회해서 해당 날짜에 대한 RankTime(off, on) 테이블 생성 및 저장
        GroupTableDTO<Days> table = when2MeetService.addUserMeet(groupId, MeetType.fromString(type), userNames);
        return baseResponseService.getSuccessResponse(table);
    }

    @PostMapping("/{type}/load")
    public BaseResponse<Object> loadUserMeet(@PathVariable("groupId") Long groupId,
                                            @PathVariable("type") String type,
                                            @RequestBody String userNames)
    {   // (그룹, 회의 ID, TYPE을 확인) -> 회의 아이디로 테이블을 조회해서 해당 date에 대해 캘린더 일정을 확인 (추출하기)
        // date로 일정 정보를 가져와서, 시간을 확인해야한다 (시간은 localTime으로 비교한다)
        GroupTableDTO<Days> table = when2MeetService.loadUserMeet(groupId, MeetType.fromString(type), userNames);
        return baseResponseService.getSuccessResponse(table);
    }

    @PostMapping("/{type}/update")
    public BaseResponse<Object> updateUserMeet(@PathVariable("groupId") Long groupId,
                                             @PathVariable("type") String type,
                                             @RequestBody String userNames)
    {   // (그룹, 회의 ID, TYPE을 확인) -> 회의 아이디, Type, 사용자로 테이블을 조회해서 when2meet을 가져온 후 ranktime을 업데이트한다
        // ranktime을 set한다 (time, rank)
        when2MeetService.updateUserMeet(groupId, MeetType.fromString(type), userNames);
        return baseResponseService.getSuccessResponse();
    }

    @PostMapping("/done")
    public BaseResponse<Object> doneGroupMeet(@PathVariable("groupId") Long groupId,
                                               @PathVariable("type") String type,
                                               @RequestBody String userNames)
    {   // (그룹, 회의 ID, TYPE을 확인) -> 해당 회의 아이디에 대해서 회의 일정을 생성한다
        // 사용자 아이디는 request에서 얻어오고, 캘린더 아이디를 접근한다
        // done을 누르는 순간 '일정 테이블'을 생성한다 (더이상 수정할 수 없음)
        when2MeetService.doneGroupMeet(groupId, MeetType.fromString(type), userNames);
        return baseResponseService.getSuccessResponse();
    }


}
