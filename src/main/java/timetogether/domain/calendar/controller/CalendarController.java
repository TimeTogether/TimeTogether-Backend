package timetogether.domain.calendar.controller;

import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import timetogether.domain.calendar.dto.request.CalendarCreateRequestDto;
import timetogether.domain.calendar.dto.request.CalendarUpdateRequestDto;
import timetogether.domain.calendar.dto.response.CalendarCreateResponseDto;
import timetogether.domain.calendar.dto.response.CalendarUpdateResponseDto;
import timetogether.domain.calendar.exception.CalendarNotExist;
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
   * 캘린더 개인 일정 생성
   *
   * @param socialId
   * @param year
   * @param month
   * @param date
   * @param request
   * @return
   */
  @PostMapping("/create/{year}/{month}/{date}")
  public BaseResponse<Object> createMeeting(
          @RequestParam(value = "socialId") String socialId, //임시
          @PathVariable(value = "year") int year,
          @PathVariable(value = "month") int month,
          @PathVariable(value = "date") int date,
          @RequestBody CalendarCreateRequestDto request
  ) {
    try {
      Long calendarId = calendarViewService.putandGetCalendarId(socialId);
      CalendarCreateResponseDto calendarCreateResponseDto = calendarService.createMeeting(socialId, request);
      return baseResponseService.getSuccessResponse(calendarCreateResponseDto);
    } catch (CalendarNotExist e) {
      return baseResponseService.getFailureResponse(e.getStatus());
    } catch (Exception e) {
      return baseResponseService.getFailureResponse(BaseResponseStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PatchMapping("/update/{meetId}") //수정중!!!
  public BaseResponse<Object> updateMeeting(
          @RequestParam(value = "socialId") String socialId, //임시
          @PathVariable(value = "meetingId") int meetingId,
          @RequestBody CalendarUpdateRequestDto request
  ) {
    try {
      Long calendarId = calendarViewService.putandGetCalendarId(socialId);
      CalendarUpdateResponseDto calendarUpdateResponseDto = calendarService.updateMeeting(socialId,request);
      return baseResponseService.getSuccessResponse(calendarUpdateResponseDto);
    } catch (CalendarNotExist e) {
      return baseResponseService.getFailureResponse(e.getStatus());
    } catch (Exception e) {
      return baseResponseService.getFailureResponse(BaseResponseStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
