package timetogether.domain.calendar.controller;

import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import timetogether.domain.calendar.dto.request.CalendarCreateRequestDto;
import timetogether.domain.calendar.dto.request.CalendarUpdateRequestDto;
import timetogether.domain.calendar.dto.response.CalendarCreateResponseDto;
import timetogether.domain.calendar.dto.response.CalendarUpdateResponseDto;
import timetogether.domain.calendar.exception.CalendarNotExist;
import timetogether.domain.calendar.exception.CalendarValidateFail;
import timetogether.domain.calendar.exception.InCalendarMeetingIdNotExist;
import timetogether.domain.calendar.service.CalendarService;
import timetogether.domain.calendar.service.CalendarViewService;
import timetogether.global.response.BaseResponse;
import timetogether.global.response.BaseResponseService;
import timetogether.global.response.BaseResponseStatus;

@RestController
@RequestMapping("/calendar")
@RequiredArgsConstructor
public class CalendarController {
  private final CalendarViewService calendarViewService;
  private final BaseResponseService baseResponseService;
  private final CalendarService calendarService;

  /**
   * 캘린더 일정 생성
   *
   * @param socialId
   * @param year
   * @param month
   * @param date
   * @param request
   * @return
   */
  @PostMapping("/create/{year}/{month}/{date}")
  public BaseResponse<Object> createCalendarMeeting(
          @RequestParam(value = "socialId") String socialId, //임시
          @PathVariable(value = "year") int year,
          @PathVariable(value = "month") int month,
          @PathVariable(value = "date") int date,
          @RequestBody CalendarCreateRequestDto request
  ) throws CalendarNotExist, CalendarValidateFail {
    CalendarCreateResponseDto calendarCreateResponseDto = calendarService.createMeeting(socialId, request);
    return baseResponseService.getSuccessResponse(calendarCreateResponseDto);
  }

  @PatchMapping("/update/{meetingId}")
  public BaseResponse<Object> updateCalendarMeeting(
          @RequestParam(value = "socialId") String socialId, //임시
          @PathVariable(value = "meetingId") Long meetingId,
          @RequestBody CalendarUpdateRequestDto request
  ) throws CalendarNotExist, CalendarValidateFail, InCalendarMeetingIdNotExist {

    CalendarUpdateResponseDto calendarUpdateResponseDto = calendarService.updateMeeting(socialId,meetingId,request);
    return baseResponseService.getSuccessResponse(calendarUpdateResponseDto);

  }

  @DeleteMapping("/delete/{meetingId}")
  public BaseResponse<Object> deleteCalendarMeeting(
          @RequestParam(value = "socialId") String socialId, //임시
          @PathVariable(value = "meetingId") Long meetingId
  ) throws CalendarNotExist, InCalendarMeetingIdNotExist {
    calendarService.deleteMeeting(meetingId);
    return baseResponseService.getSuccessResponse(BaseResponseStatus.SUCCESS);
  }
}
