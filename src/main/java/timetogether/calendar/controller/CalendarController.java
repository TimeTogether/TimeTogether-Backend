package timetogether.calendar.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import timetogether.calendar.dto.request.CalendarCreateRequestDto;
import timetogether.calendar.dto.request.CalendarUpdateRequestDto;
import timetogether.calendar.dto.response.CalendarCreateResponseDto;
import timetogether.calendar.dto.response.CalendarUpdateResponseDto;
import timetogether.calendar.exception.CalendarNotExist;
import timetogether.calendar.exception.CalendarValidateFail;
import timetogether.calendar.exception.InCalendarMeetingIdNotExist;
import timetogether.calendar.service.CalendarService;
import timetogether.calendar.service.CalendarViewService;
import timetogether.global.response.BaseResponse;
import timetogether.global.response.BaseResponseService;
import timetogether.global.response.BaseResponseStatus;
import timetogether.jwt.service.JwtService;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@RestController
@RequestMapping("/calendar")
@RequiredArgsConstructor
public class CalendarController {
  private final JwtService jwtService;
  private final CalendarViewService calendarViewService;
  private final BaseResponseService baseResponseService;
  private final CalendarService calendarService;

  /**
   * 캘린더 개인 일정 생성
   *
   * @param headerRequest
   * @param request
   * @return
   * @throws CalendarNotExist
   * @throws CalendarValidateFail
   */
  @PostMapping("/create")
  public BaseResponse<Object> createCalendarMeeting(
          HttpServletRequest headerRequest,
//          @PathVariable(value = "year") int year,
//          @PathVariable(value = "month") int month,
//          @PathVariable(value = "date") int date,
          @RequestBody CalendarCreateRequestDto request
  ) throws CalendarNotExist, CalendarValidateFail {
    String accessToken = jwtService.extractAccessToken(headerRequest).get();
    String socialId = jwtService.extractId(accessToken).get();
    log.info("[일정생성 시작]");
    CalendarCreateResponseDto calendarCreateResponseDto = calendarService.createMeeting(socialId, request);
    return baseResponseService.getSuccessResponse(calendarCreateResponseDto);
  }

  /**
   * 캘린더 개인 일정 수정
   *
   * @param headerRequest
   * @param meetingId
   * @param request
   * @return
   * @throws CalendarNotExist
   * @throws CalendarValidateFail
   * @throws InCalendarMeetingIdNotExist
   */
  @PatchMapping("/update/{meetingId}")
  public BaseResponse<Object> updateCalendarMeeting(
          HttpServletRequest headerRequest,
          @PathVariable(value = "meetingId") Long meetingId,
          @RequestBody CalendarUpdateRequestDto request
  ) throws CalendarNotExist, CalendarValidateFail, InCalendarMeetingIdNotExist {
    String accessToken = jwtService.extractAccessToken(headerRequest).get();
    String socialId = jwtService.extractId(accessToken).get();
    CalendarUpdateResponseDto calendarUpdateResponseDto = calendarService.updateMeeting(socialId,meetingId,request);
    return baseResponseService.getSuccessResponse(calendarUpdateResponseDto);

  }

  /**
   * 캘린더 개인 일정 삭제
   *
   * @param headerRequest
   * @param meetingId
   * @return
   * @throws CalendarNotExist
   * @throws InCalendarMeetingIdNotExist
   */
  @DeleteMapping("/delete/{meetingId}")
  public BaseResponse<Object> deleteCalendarMeeting(
          HttpServletRequest headerRequest,
          @PathVariable(value = "meetingId") Long meetingId
  ) throws CalendarNotExist, InCalendarMeetingIdNotExist {
    calendarService.deleteMeeting(meetingId);
    return baseResponseService.getSuccessResponse(BaseResponseStatus.SUCCESS);
  }
}