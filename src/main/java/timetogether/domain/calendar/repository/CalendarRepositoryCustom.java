package timetogether.domain.calendar.repository;

import timetogether.domain.calendar.dto.response.CalendarViewResponseDto;

public interface CalendarRepositoryCustom {
  public CalendarViewResponseDto findMeetings(Long calendarId, int year, int month);

  public CalendarViewResponseDto findMeetings(Long calendarId, int year, int month, int date);

  public CalendarViewResponseDto findMeetings(Long calendarId, Long meetingId);
}
