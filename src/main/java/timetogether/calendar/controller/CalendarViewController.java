package timetogether.calendar.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import timetogether.calendar.dto.response.CalendarMergeResponseDto;
import timetogether.calendar.dto.response.CalendarViewResponseDto;
import timetogether.calendar.dto.response.CalendarWhere2meetDto;
import timetogether.calendar.exception.CalendarNotExist;
import timetogether.calendar.service.CalendarViewService;
import timetogether.global.response.BaseResponse;
import timetogether.global.response.BaseResponseService;
import timetogether.jwt.service.JwtService;

import java.net.http.HttpHeaders;
import java.util.List;
import java.util.Optional;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@RestController
@RequestMapping("/calendar")
@RequiredArgsConstructor
@Slf4j
public class CalendarViewController {
  private final CalendarViewService calendarViewService;
  private final BaseResponseService baseResponseService;
  private final JwtService jwtService;

  /**
   * user의 캘린더 (특정 월) 일정 전체 조회
   *
   * @param year,month
   * @return BaseResponse<Object>
   */
  @GetMapping("/view/{year}/{month}")
  public BaseResponse<Object> viewAllSpecificMonthMeetings(
          HttpServletRequest headerRequest,
          @PathVariable(value = "year") int year,
          @PathVariable(value = "month") int month
  ) throws CalendarNotExist {
    String accessToken = jwtService.extractAccessToken(headerRequest).get();
    String socialId = jwtService.extractId(accessToken).get();
    Long calendarId = calendarViewService.putandGetCalendarId(socialId);
    CalendarViewResponseDto calendarViewResponseDto = calendarViewService.getMeetingsYearMonth(calendarId,year,month);
    List<CalendarWhere2meetDto> calendarWhere2meetDto = calendarViewService.getWhere2meetInMeetings(calendarViewResponseDto);
    CalendarMergeResponseDto merged = calendarViewService.merge(calendarViewResponseDto, calendarWhere2meetDto);
    return baseResponseService.getSuccessResponse(merged);

  }
  /**
   * user의 캘린더 (특정 월,날짜) 일정 전체 조회
   *
   * @param year, month
   * @return BaseResponse<Object>
   */
  @GetMapping("/view/{year}/{month}/{date}")
  public BaseResponse<Object> viewAllSpecificDateMeetings(
          HttpServletRequest headerRequest,
          @PathVariable(value = "year") int year,
          @PathVariable(value = "month") int month,
          @PathVariable(value = "date") int date
  ) throws CalendarNotExist {

    String accessToken = jwtService.extractAccessToken(headerRequest).get();
    String socialId = jwtService.extractId(accessToken).get();
    Long calendarId = calendarViewService.putandGetCalendarId(socialId);
    CalendarViewResponseDto calendarViewResponseDto = calendarViewService.getMeetingsYearMonthDate(calendarId,year,month,date);
    return baseResponseService.getSuccessResponse(calendarViewResponseDto);

  }


  /**
   * user의 캘린더 (특정 월,날짜,미팅 아이디) 일정 조회
   *
   * @param meetingId
   * @return BaseResponse<Object>
   */
  @GetMapping("/view/{meetingId}")
  public BaseResponse<Object> viewSpecificMeeting(
          HttpServletRequest headerRequest,
          @PathVariable(value = "meetingId") Long meetingId
  ) throws CalendarNotExist {
    String accessToken = jwtService.extractAccessToken(headerRequest).get();
    String socialId = jwtService.extractId(accessToken).get();
    Long calendarId = calendarViewService.putandGetCalendarId(socialId);
    CalendarViewResponseDto calendarViewResponseDto = calendarViewService.getMeeting(calendarId,meetingId);
    return baseResponseService.getSuccessResponse(calendarViewResponseDto);

  }

}
