package timetogether.domain.calendar.repository;

import timetogether.domain.calendar.dto.CalendarViewDto;

import java.util.List;

public interface CalendarRepositoryCustom {
  CalendarViewDto findMeetings(Long calendarId, int yearNow, int monthNow);
  CalendarViewDto findMeetings(Long calendarId, int yearNow, int monthNow, int dateNow);

}
