package timetogether.domain.calendar.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import timetogether.domain.calendar.dto.CalendarViewDto;
import timetogether.domain.calendar.exception.CalendarNotExist;
import timetogether.domain.calendar.service.CalendarViewService;
import timetogether.global.response.BaseException;
import timetogether.global.response.BaseResponse;
import timetogether.global.response.BaseResponseService;
import timetogether.global.response.BaseResponseStatus;
import timetogether.jwt.service.JwtService;
import timetogether.oauth2.entity.User;

import java.util.Optional;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@RestController
@RequestMapping("/calendar/view")
@RequiredArgsConstructor
public class CalendarViewController {
  private final CalendarViewService calendarViewService;
  private final BaseResponseService baseResponseService;
  private final JwtService jwtService;

  /**
   * user의 캘린더 (특정 월) 일정 전체 조회
   *
   * @param token
   * @return BaseResponse<Object>
   */
  @GetMapping("/{year}/{month}")
  public BaseResponse<Object> viewAllSpecificMonthMeetings(
          @RequestHeader(value = "Authorization") String token,
          @PathVariable(value = "year") int year,
          @PathVariable(value = "month") int month
  ){
    CalendarViewDto calendarViewDto = null;
    try{
      User matchedUser;
      Long calendarId;

      if(jwtService.isTokenValid(token)) {
        String actualToken = token.startsWith("Bearer ") ? token.replace("Bearer ", "") : token;
        matchedUser = calendarViewService.getUserOptional(actualToken).get();
        calendarId = calendarViewService.putCalendarId(matchedUser);

        calendarViewDto = calendarViewService.getMeetingsYearMonth(calendarId,year,month);
      }

    }catch (Exception e){
      return baseResponseService.getFailureResponse(BaseResponseStatus.INTERNAL_SERVER_ERROR);
    }
    return baseResponseService.getSuccessResponse(calendarViewDto);

  }

  /**
   * user의 캘린더 (특정 월,날짜) 일정 전체 조회
   *
   * @param token
   * @return BaseResponse<Object>
   */
  @GetMapping("/{year}/{month}/{date}")
  public BaseResponse<Object> viewAllSpecificDateMeetings(
          @RequestHeader(value = "accessToken") String token,
          @PathVariable(value = "year") int year,
          @PathVariable(value = "month") int month,
          @PathVariable(value = "date") int date
  ){
    User matchedUser; //accessToken과 매칭되는 matchedUser;
    Long matchedCalendarId;
    try{

      if (!jwtService.isTokenValid(token)) {//accessToken 유효한지 확인
        throw new CalendarNotExist();//HttpStatus: NOT_FOUND, message: "유효한 사용자가 아니라서 캘린더를 불러오지 못했습니다."
      }else{
        String actualToken = token.startsWith("Bearer ") ? token.replace("Bearer ", "") : token;
        matchedUser = calendarViewService.getUserOptional(actualToken).get();
        calendarViewService.putCalendarId(matchedUser);
      }
      //유효한 사용자인 경우 캘린더 조회
      matchedCalendarId = matchedUser.getCalendar().getId();
      CalendarViewDto calendarViewDto = calendarViewService.getMeetingsYearMonthDate(matchedCalendarId, year, month, date);

      //조회된 일정이 없는 경우
      if (calendarViewDto.getMeetingList().isEmpty()){
        return baseResponseService.getFailureResponse(BaseResponseStatus.NOT_FOUND_MEETINGS_IN_CALENDAR);
      }
      //조회된 일정이 있는 경우
      return baseResponseService.getSuccessResponse(calendarViewDto);
    }catch(CalendarNotExist e){
      return baseResponseService.getFailureResponse(e.getStatus());
    }catch (Exception e){
      return baseResponseService.getFailureResponse(BaseResponseStatus.INTERNAL_SERVER_ERROR);
    }

  }


  /**
   * user의 캘린더 (특정 월,날짜,미팅 아이디) 일정 조회
   *
   * @param token
   * @return BaseResponse<Object>
   */
  @GetMapping("/{year}/{month}/{date}/{meetingId}")
  public BaseResponse<Object> viewSpecificMeeting(
          @RequestHeader(value = "accessToken") String token,
          @PathVariable(value = "year") int year,
          @PathVariable(value = "month") int month,
          @PathVariable(value = "date") int date,
          @PathVariable(value = "meetingId") Long meetingId
  ){
    User matchedUser; //accessToken과 매칭되는 matchedUser;
    Long matchedCalendarId;
    try{

      if (!jwtService.isTokenValid(token)) {//accessToken 유효한지 확인
        throw new CalendarNotExist();//HttpStatus: NOT_FOUND, message: "유효한 사용자가 아니라서 캘린더를 불러오지 못했습니다."
      }else{
        String actualToken = token.startsWith("Bearer ") ? token.replace("Bearer ", "") : token;
        matchedUser = calendarViewService.getUserOptional(actualToken).get();
        calendarViewService.putCalendarId(matchedUser);
      }
      //유효한 사용자인 경우 캘린더 조회
      matchedCalendarId = matchedUser.getCalendar().getId();
      CalendarViewDto calendarViewDto = calendarViewService.getMeetingsYearMonthDateMeetingId(matchedCalendarId, year, month, date, meetingId);

      //조회된 일정이 없는 경우
      if (calendarViewDto.getMeetingList().isEmpty()){
        return baseResponseService.getFailureResponse(BaseResponseStatus.NOT_FOUND_MEETINGS_IN_CALENDAR);
      }
      //조회된 일정이 있는 경우
      return baseResponseService.getSuccessResponse(calendarViewDto);
    }catch(CalendarNotExist e){
      return baseResponseService.getFailureResponse(e.getStatus());
    }catch (Exception e){
      return baseResponseService.getFailureResponse(BaseResponseStatus.INTERNAL_SERVER_ERROR);
    }

  }


}
