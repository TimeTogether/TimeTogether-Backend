package timetogether.domain.calendar.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timetogether.domain.calendar.Calendar;
import timetogether.domain.calendar.dto.CalendarViewDto;
import timetogether.domain.calendar.repository.CalendarRepository;
import timetogether.domain.calendar.repository.CalendarRepositoryCustomImpl;
import timetogether.jwt.service.JwtService;
import timetogether.oauth2.entity.User;
import timetogether.oauth2.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CalendarViewService {
  private final CalendarRepository calendarRepository;
  private final CalendarRepositoryCustomImpl calendarRepositoryCustomImpl;
  private final UserRepository userRepository;
  private final JwtService jwtService;
  private final EntityManager entityManager;

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


  @Transactional
  public Long putCalendarId(User matchedUser) {
    if (matchedUser.getCalendar() != null) {//이미 calendarId가 존재하는 경우 반환
      return matchedUser.getCalendar().getId();
    }
    Calendar newCalendar = new Calendar();
    Calendar savedCalendar = calendarRepository.save(newCalendar);
    matchedUser.updateCalendarId(savedCalendar);
    userRepository.save(matchedUser);
    return newCalendar.getId();
  }

  public Optional<User> getUserOptional(String accessToken) {
    Optional<String> socialId = jwtService.extractId(accessToken);
    if (socialId.isEmpty()) {
      return Optional.empty();
    }

    String actualId = socialId.get();

    try {
      TypedQuery<User> query = entityManager.createQuery(
              "SELECT u FROM User u WHERE u.socialId = :socialId", User.class);
      query.setParameter("socialId", actualId);
      List<User> results = query.getResultList();
      return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    } catch (Exception e) {
      return Optional.empty();
    }
  }
}