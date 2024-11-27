package timetogether.mypage.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import timetogether.global.response.BaseResponse;
import timetogether.global.response.BaseResponseService;
import timetogether.jwt.service.JwtService;
import timetogether.meeting.service.MeetingService;
import timetogether.mypage.UserNameDTO;
import timetogether.mypage.service.MypageService;
import timetogether.when2meet.dto.Result;

import java.util.List;

@Controller
@AllArgsConstructor
@ResponseBody
@RequestMapping("/user/history")
public class MypageController {

    private final JwtService jwtService;
    private final MypageService mypageService;
    private final BaseResponseService baseResponseService;

    @GetMapping
    public BaseResponse<Object> findMeetByUser(HttpServletRequest request){
        String accessToken = jwtService.extractAccessToken(request).get();
        String socialId = jwtService.extractId(accessToken).get();
        List<Result> resultList;
        resultList = mypageService.findMeetByUser(socialId);         // TODO: 그룹별로 보게할 건가?

        return baseResponseService.getSuccessResponse(resultList);
    }

    @GetMapping("/name")
    public BaseResponse<Object> getByUser(HttpServletRequest request){
        String accessToken = jwtService.extractAccessToken(request).get();
        String socialId = jwtService.extractId(accessToken).get();
        String userName = mypageService.getByUser(socialId);         // TODO: 그룹별로 보게할 건가?

        return baseResponseService.getSuccessResponse(userName);
    }


    @GetMapping("{title}/search")
    public BaseResponse<Object> searchMeetByUser(HttpServletRequest request, @PathVariable("title") String title){
        String accessToken = jwtService.extractAccessToken(request).get();
        String socialId = jwtService.extractId(accessToken).get();
        List<Result> resultList = mypageService.searchMeetByUser(socialId, title);
        return baseResponseService.getSuccessResponse(resultList);
    }

}
