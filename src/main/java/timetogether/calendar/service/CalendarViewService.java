package timetogether.calendar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timetogether.calendar.Calendar;
import timetogether.calendar.dto.response.CalendarViewResponseDto;
import timetogether.calendar.exception.CalendarNotExist;
import timetogether.calendar.repository.CalendarRepository;
import timetogether.global.response.BaseResponseStatus;
import timetogether.oauth2.entity.User;
import timetogether.oauth2.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CalendarViewService {
  private final CalendarRepository calendarRepository;
  private final UserRepository userRepository;
  /**
   * 현재 년, 월에 등록된 전체 일정 리스트 DTO로 반환
   *
   * @return List<CalendarRepository>
   */
  public CalendarViewResponseDto getMeetingsYearMonth(Long calendarId, int year, int month) {
    return calendarRepository.findMeetings(calendarId,year, month);
  }
  public CalendarViewResponseDto getMeetingsYearMonthDate(Long calendarId, int year, int month, int date) {
    return calendarRepository.findMeetings(calendarId,year, month,date);
  }

  @Transactional
  public Long putandGetCalendarId(String socialId) throws CalendarNotExist {
    User matchedUser = userRepository.findBySocialId(socialId)
            .orElseThrow(() -> new CalendarNotExist(BaseResponseStatus.NOT_VALID_USER));
    if (matchedUser.getCalendar() != null) {//이미 calendarId가 존재하는 경우 반환
      return matchedUser.getCalendar().getCalendarId();
    }
    Calendar savedCalendar = calendarRepository.save(new Calendar());
    matchedUser.updateCalendarId(savedCalendar);
    userRepository.save(matchedUser);
    return savedCalendar.getCalendarId();
  }


  public CalendarViewResponseDto getMeeting(Long calendarId, Long meetingId) {
    return calendarRepository.findMeetings(calendarId,meetingId);
  }
}