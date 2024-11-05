package timetogether.calendar.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import timetogether.meeting.Meeting;
import timetogether.calendar.dto.response.CalendarViewResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class CalendarRepositoryCustomImpl implements CalendarRepositoryCustom{
  private final EntityManager em;

  @Override
  public CalendarViewResponseDto findMeetings(Long calendarId, int year, int month) {
    String query = "SELECT DISTINCT m FROM Meeting m " +  // DISTINCT 추가
            "JOIN FETCH m.calendar c " +
            "WHERE c.id = :calendarId " +
            "AND (" +
            "   (YEAR(m.meetDTstart) = :year AND MONTH(m.meetDTstart) = :month) " +
            "   OR (YEAR(m.meetDTend) = :year AND MONTH(m.meetDTend) = :month) " +
            "   OR (m.meetDTstart <= :startDate AND m.meetDTend >= :endDate)" +  // 해당 월의 중간에 걸쳐있는 경우 추가
            ")";

    // 해당 월의 시작일과 끝일 계산
    LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0);
    LocalDateTime endDate = startDate.plusMonths(1).minusSeconds(1);

    List<Meeting> meetingList = em.createQuery(query, Meeting.class)
            .setParameter("calendarId", calendarId)
            .setParameter("year", year)
            .setParameter("month", month)
            .setParameter("startDate", startDate)
            .setParameter("endDate", endDate)
            .getResultList();

    return CalendarViewResponseDto.of(year, month, meetingList);
  }

  @Override
  public CalendarViewResponseDto findMeetings(Long calendarId, int year, int month, int date) {
    String query = "SELECT m FROM Meeting m " +
            "JOIN FETCH m.calendar c " +
            "WHERE c.id = :calendarId " +
            "AND (" +
            "   (YEAR(m.meetDTstart) = :year AND MONTH(m.meetDTstart) = :month AND DAY(m.meetDTstart) = :date) " +    // 시작일이 해당 날짜인 경우
            "   OR (YEAR(m.meetDTend) = :year AND MONTH(m.meetDTend) = :month AND DAY(m.meetDTend) = :date) " +
            ")";       // 종료일이 해당 날짜인 경우


    LocalDateTime targetDate = LocalDateTime.of(year, month, date, 0, 0, 0);

    List<Meeting> meetingList = em.createQuery(query, Meeting.class)
            .setParameter("calendarId", calendarId)
            .setParameter("year", year)
            .setParameter("month", month)
            .setParameter("date", date)
            .getResultList();

    return CalendarViewResponseDto.of(year, month, meetingList);
  }

  @Override
  public CalendarViewResponseDto findMeetings(Long calendarId, Long meetingId) {
    String query = "SELECT m FROM Meeting m " +
            "JOIN FETCH m.calendar c " +
            "WHERE c.id = :calendarId " +
            "AND m.id = :meetingId";
    List<Meeting> meetingList = em.createQuery(query, Meeting.class)
            .setParameter("calendarId", calendarId)
            .setParameter("meetingId", meetingId)
            .getResultList();

    // 특정 meeting을 조회하는 경우에는 year와 month를 meeting의 시작 날짜에서 가져옵니다
    int year = 0;
    int month = 0;
    if (!meetingList.isEmpty()) {
      LocalDateTime meetingDate = meetingList.get(0).getMeetDTstart();
      year = meetingDate.getYear();
      month = meetingDate.getMonthValue();
    }

    return CalendarViewResponseDto.of(year, month, meetingList);
  }
}
