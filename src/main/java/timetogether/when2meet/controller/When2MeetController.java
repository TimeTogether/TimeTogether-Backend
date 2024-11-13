package timetogether.when2meet.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import timetogether.global.response.BaseResponse;
import timetogether.global.response.BaseResponseService;
import timetogether.when2meet.dto.Result;
import timetogether.when2meet.service.When2MeetService;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/group/{groupId}/when")
@Controller
public class When2MeetController {

    private final When2MeetService when2MeetService;
    private final BaseResponseService baseResponseService;

    @GetMapping
    public BaseResponse<Object> viewWhenResult(@PathVariable("groupId") Long groupId){
        List<Result> view = when2MeetService.view(groupId);
        return baseResponseService.getSuccessResponse(view);
    }


    @PostMapping("/meet/add")
    public BaseResponse<Object> addGroupMeet(@RequestParam String date, @PathVariable("groupId") Long groupId){
        when2MeetService.addGroupMeet(date, groupId);
        return baseResponseService.getSuccessResponse();
    }

}
