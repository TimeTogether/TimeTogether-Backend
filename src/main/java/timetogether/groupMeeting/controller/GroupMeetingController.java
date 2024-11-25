package timetogether.groupMeeting.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import timetogether.calendar.exception.CalendarNotExist;
import timetogether.calendar.exception.CalendarValidateFail;
import timetogether.global.response.BaseResponse;
import timetogether.global.response.BaseResponseService;
import timetogether.groupMeeting.dto.GroupMeetingCreateRequest;
import timetogether.groupMeeting.dto.GroupMeetingCreateResponse;
import timetogether.groupMeeting.service.GroupMeetingService;
import timetogether.jwt.service.JwtService;

import java.io.IOException;
import java.net.URISyntaxException;

import static timetogether.groupMeeting.service.GroupMeetingService.getRandomGroupWhereCandidates;

@RestController
@RequestMapping("/group/groupmeeting")
@RequiredArgsConstructor
public class GroupMeetingController {

  private BaseResponseService baseResponseService;
  private final JwtService jwtService;
  private final GroupMeetingService groupMeetingService;

  /**
   * 그룹 내 그룹 회의 한 개 생성
   *
   * @param headerRequest
   * @param request
   * @return
   * @throws CalendarNotExist
   * @throws CalendarValidateFail
   */
  @PostMapping("/create")
  public BaseResponse<Object> createGroupMeeting(
          HttpServletRequest headerRequest,
          @RequestBody GroupMeetingCreateRequest request
  ) throws IOException, URISyntaxException {
    String accessToken = jwtService.extractAccessToken(headerRequest).get();
    String socialId = jwtService.extractId(accessToken).get();
    GroupMeetingCreateResponse calendarCreateResponseDto = groupMeetingService.createMeeting(socialId, request);
    getRandomGroupWhereCandidates();
    return baseResponseService.getSuccessResponse(calendarCreateResponseDto);
  }




}
