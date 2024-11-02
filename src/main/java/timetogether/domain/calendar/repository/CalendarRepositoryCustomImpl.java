package timetogether.domain.calendar.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import timetogether.domain.Meeting;
import timetogether.domain.calendar.dto.CalendarViewDto;

import java.util.List;

@Transactional
public class CalendarRepositoryCustomImpl implements CalendarRepositoryCustom{
  @PersistenceContext
  private EntityManager em;

  @Override
  public CalendarViewDto findMeetings(Long calendarId, int year, int month) {
    //해당 월의 미팅 목록을 조회
    List<Meeting> meetings = em.createQuery(
            "SELECT m FROM Meeting m " +
                    "JOIN m.calendar c " +
                    "WHERE c.id = :calendarId " +
                    "AND YEAR(m.meetDTstart) = :yearNow " +
                    "AND MONTH(m.meetDTstart) = :monthNow " +
                    "ORDER BY m.meetDTstart", Meeting.class)
            .setParameter("calendarId", calendarId)
            .setParameter("yearNow", year)
            .setParameter("monthNow", month)
            .getResultList();

    // CalendarViewDto 생성 및 반환
    return new CalendarViewDto(year, month, -1, meetings); //date 가 필요없는 경우 -1로 설정

  }

  public CalendarViewDto findMeetings(Long calendarId, int year, int month, int date) {
    //해당 일의 미팅 목록을 조회
    List<Meeting> meetings = em.createQuery(
                    "SELECT m FROM Meeting m " +
                            "JOIN m.calendar c " +
                            "WHERE c.id = :calendarId " +
                            "AND YEAR(m.meetDTstart) = :yearNow " +
                            "AND MONTH(m.meetDTstart) = :monthNow " +
                            "AND DAY(m.meetDTstart) = :dateNow " +
                            "ORDER BY m.meetDTstart", Meeting.class)
            .setParameter("calendarId", calendarId)
            .setParameter("yearNow", year)
            .setParameter("monthNow", month)
            .setParameter("dateNow", date)
            .getResultList();

    // CalendarViewDto 생성 및 반환
    return new CalendarViewDto(year, month, date, meetings);
  }

  public CalendarViewDto findMeetings(Long calendarId, int year, int month, int date, Long meetingId) {
    //해당 미팅아이디의 미팅을 조회
    Meeting meeting = em.createQuery(
                    "SELECT m FROM Meeting m " +
                        "WHERE m.id = :meetingId "
                        ,Meeting.class)
            .setParameter("meetingId", meetingId)
            .getSingleResult();

    // CalendarViewDto 생성 및 반환
    return new CalendarViewDto(year, month, date, meeting);
  }
}