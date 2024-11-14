package timetogether.when2meet.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import timetogether.global.response.BaseResponse;
import timetogether.global.response.BaseResponseService;
import timetogether.jwt.service.JwtService;
import timetogether.when2meet.dto.Result;
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
    public BaseResponse<Object> viewWhenResult(@PathVariable("groupId") Long groupId){
        List<Result> view = when2MeetService.view(groupId);
        return baseResponseService.getSuccessResponse(view);
    }


    @PostMapping("/meet/add")
    public BaseResponse<Object> addGroupMeet(HttpServletRequest request, @RequestBody List<String> dates, @PathVariable("groupId") Long groupId){
        Optional<String> accessToken = jwtService.extractAccessToken(request);
        Optional<String> socialId = jwtService.extractId(accessToken.get());
        when2MeetService.addGroupMeet(socialId.get(), dates, groupId);
        return baseResponseService.getSuccessResponse();
    }

    @PostMapping("/meet")
    public BaseResponse<Object> viewMeetResult(HttpServletRequest request, @PathVariable("groupId") Long groupId)
    {
        Optional<String> accessToken = jwtService.extractAccessToken(request);
        Optional<String> socialId = jwtService.extractId(accessToken.get());
        when2MeetService.viewMeet(socialId.get(), groupId);
        return baseResponseService.getSuccessResponse();
    }
}
