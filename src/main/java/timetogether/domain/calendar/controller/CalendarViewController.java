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
          @PathParam(value = "socialId") String socialId, //임시
          @PathVariable(value = "year") int year,
          @PathVariable(value = "month") int month
  ){
    try{
      Long calendarId = calendarViewService.putandGetCalendarId(socialId);
      CalendarViewResponseDto calendarViewResponseDto = calendarViewService.getMeetingsYearMonth(calendarId,year,month);
      return baseResponseService.getSuccessResponse(calendarViewResponseDto);
    }catch (CalendarNotExist e){
      return baseResponseService.getFailureResponse(e.getStatus());
    }
    catch (Exception e){
      return baseResponseService.getFailureResponse(BaseResponseStatus.INTERNAL_SERVER_ERROR);
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
          @PathParam(value = "socialId") String socialId,
          @PathVariable(value = "year") int year,
          @PathVariable(value = "month") int month,
          @PathVariable(value = "date") int date
  ){
    try{
      Long calendarId = calendarViewService.putandGetCalendarId(socialId);
      CalendarViewResponseDto calendarViewResponseDto = calendarViewService.getMeetingsYearMonthDate(calendarId,year,month,date);
      return baseResponseService.getSuccessResponse(calendarViewResponseDto);
    }catch (CalendarNotExist e){
      return baseResponseService.getFailureResponse(e.getStatus());
    }
    catch (Exception e){
      return baseResponseService.getFailureResponse(BaseResponseStatus.INTERNAL_SERVER_ERROR);
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
          @PathParam(value = "socialId") String socialId,//임시
          @PathVariable(value = "meetingId") Long meetingId
  ){
    try{
      Long calendarId = calendarViewService.putandGetCalendarId(socialId);
      CalendarViewResponseDto calendarViewResponseDto = calendarViewService.getMeeting(calendarId,meetingId);
      return baseResponseService.getSuccessResponse(calendarViewResponseDto);
    }catch (CalendarNotExist e){
      return baseResponseService.getFailureResponse(e.getStatus());
    }
    catch (Exception e){
      return baseResponseService.getFailureResponse(BaseResponseStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
