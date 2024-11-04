package timetogether.domain.calendar.controller;

import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import timetogether.domain.calendar.dto.request.CalendarCreateRequestDto;
import timetogether.domain.calendar.dto.response.CalendarCreateResponseDto;
import timetogether.domain.calendar.dto.response.CalendarViewResponseDto;
import timetogether.domain.calendar.exception.CalendarNotExist;
import timetogether.domain.calendar.service.CalendarService;
import timetogether.domain.calendar.service.CalendarViewService;
import timetogether.global.response.BaseResponse;
import timetogether.global.response.BaseResponseService;
import timetogether.global.response.BaseResponseStatus;
import timetogether.jwt.service.JwtService;

@RestController
@RequestMapping("/calendar")
@RequiredArgsConstructor
public class CalendarViewController {
  private final CalendarViewService calendarViewService;
  private final BaseResponseService baseResponseService;

  /**
   * user의 캘린더 (특정 월) 일정 전체 조회
   *
   * @param socialId,year,month
   * @return BaseResponse<Object>
   */
  @GetMapping("/view/{year}/{month}")
  public BaseResponse<Object> viewAllSpecificMonthMeetings(
          @RequestParam(value = "socialId") String socialId, //임시
          @PathVariable(value = "year") int year,
          @PathVariable(value = "month") int month
  ) throws Exception {
    try{
      Long calendarId = calendarViewService.putandGetCalendarId(socialId);
      CalendarViewResponseDto calendarViewResponseDto = calendarViewService.getMeetingsYearMonth(calendarId,year,month);
      return baseResponseService.getSuccessResponse(calendarViewResponseDto);
    }catch (CalendarNotExist e){
      throw new CalendarNotExist(e.getStatus());
    }
    catch (Exception e){
      throw new Exception();
    }
  }
  /**
   * user의 캘린더 (특정 월,날짜) 일정 전체 조회
   *
   * @param socialId, year, month
   * @return BaseResponse<Object>
   */
  @GetMapping("/view/{year}/{month}/{date}")
  public BaseResponse<Object> viewAllSpecificDateMeetings(
          @RequestParam(value = "socialId") String socialId,
          @PathVariable(value = "year") int year,
          @PathVariable(value = "month") int month,
          @PathVariable(value = "date") int date
  ) throws Exception {
    try{
      Long calendarId = calendarViewService.putandGetCalendarId(socialId);
      CalendarViewResponseDto calendarViewResponseDto = calendarViewService.getMeetingsYearMonthDate(calendarId,year,month,date);
      return baseResponseService.getSuccessResponse(calendarViewResponseDto);
    }catch (CalendarNotExist e){
      throw new CalendarNotExist(e.getStatus());
    }
    catch (Exception e){
      throw new Exception();
    }
  }


  /**
   * user의 캘린더 (특정 월,날짜,미팅 아이디) 일정 조회
   *
   * @param socialId, year, month, date, meetingId
   * @return BaseResponse<Object>
   */
  @GetMapping("/view/{meetingId}")
  public BaseResponse<Object> viewSpecificMeeting(
          @RequestParam(value = "socialId") String socialId,//임시
          @PathVariable(value = "meetingId") Long meetingId
  ) throws Exception {
    try{
      Long calendarId = calendarViewService.putandGetCalendarId(socialId);
      CalendarViewResponseDto calendarViewResponseDto = calendarViewService.getMeeting(calendarId,meetingId);
      return baseResponseService.getSuccessResponse(calendarViewResponseDto);
    }catch (CalendarNotExist e){
      throw new CalendarNotExist(e.getStatus());
    }
    catch (Exception e){
      throw new Exception();
    }
  }

}
