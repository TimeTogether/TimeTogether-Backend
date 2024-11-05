package timetogether.calendar.repository;

import timetogether.calendar.dto.response.CalendarViewResponseDto;

public interface CalendarRepositoryCustom {
  public CalendarViewResponseDto findMeetings(Long calendarId, int year, int month);

  public CalendarViewResponseDto findMeetings(Long calendarId, int year, int month, int date);

  public CalendarViewResponseDto findMeetings(Long calendarId, Long meetingId);
}
