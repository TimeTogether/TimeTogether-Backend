package timetogether.domain.calendar.service;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timetogether.domain.calendar.Calendar;
import timetogether.domain.calendar.dto.CalendarViewDto;
import timetogether.domain.calendar.repository.CalendarRepository;
import timetogether.domain.calendar.repository.CalendarRepositoryCustomImpl;
import timetogether.oauth2.entity.User;
import timetogether.oauth2.repository.UserRepository;

import java.util.Optional;

@Service
@NoArgsConstructor
public class CalendarViewService {
  private CalendarRepository calendarRepository;
  private CalendarRepositoryCustomImpl calendarRepositoryCustomImpl;
  private UserRepository userRepository;

  /**
   * 현재 년, 월에 등록된 전체 일정 리스트 DTO로 반환
   *
   * @return List<CalendarViewDto>
   */
  public CalendarViewDto getMeetingsYearMonth(Long CalendarId, int year, int month) {
    return calendarRepositoryCustomImpl.findMeetings(CalendarId,year, month);
  }

  public CalendarViewDto getMeetingsYearMonthDate(Long CalendarId, int year, int month, int date) {
    return calendarRepositoryCustomImpl.findMeetings(CalendarId,year, month, date);
  }

  public CalendarViewDto getMeetingsYearMonthDateMeetingId(Long CalendarId, int year, int month, int date, Long meetingId) {
    return calendarRepositoryCustomImpl.findMeetings(CalendarId,year, month, date,meetingId);
  }

  public Optional<User> getUserOptional(String token) {
    return userRepository.findByRefreshToken(token);
  }

  @Transactional
  public void putCalendarId(User matchedUser) {
    if (matchedUser.getCalendar() != null) {
      return;
    }
    Calendar newCalendar = new Calendar();
    Calendar savedCalendar = calendarRepository.save(newCalendar);
    matchedUser.updateCalendarId(savedCalendar);
    userRepository.save(matchedUser);
  }
}