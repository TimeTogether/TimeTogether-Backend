package timetogether.domain.calendar.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import timetogether.domain.Meeting;
import timetogether.domain.calendar.dto.response.CalendarViewResponseDto;

import java.util.List;

@RequiredArgsConstructor
public class CalendarRepositoryCustomImpl implements CalendarRepositoryCustom{
  private final EntityManager em;

  @Override
  public CalendarViewResponseDto findMeetings(Long calendarId, int year, int month) {
    //해당 월의 시작일과 끝일 패턴 생성
    String yearMonthPattern = String.format("%d-%02d", year, month);
    String monthStart = String.format("%d-%02d-01", year, month);
    String monthEnd = String.format("%d-%02d-31", year, month);

    String query = "SELECT m FROM Meeting m " +
            "JOIN FETCH m.calendar c " +
            "WHERE c.id = :calendarId " +
            "AND (m.meetDTstart LIKE :yearMonth% " +                  //시작일이 해당 월인 경우
            "OR m.meetDTend LIKE :yearMonth% " +                      //종료일이 해당 월인 경우
            "OR (m.meetDTstart <= :monthEnd AND m.meetDTend >= :monthStart))"; //기간이 해당 월을 포함하는 경우

    List<Meeting> meetingList = em.createQuery(query, Meeting.class)
            .setParameter("calendarId", calendarId)
            .setParameter("yearMonth", yearMonthPattern)
            .setParameter("monthStart", monthStart)
            .setParameter("monthEnd", monthEnd)
            .getResultList();


    return new CalendarViewResponseDto().builder()
            .month(month)
            .year(year)
            .meetingList(meetingList)
            .build();

  }

  @Override
  public CalendarViewResponseDto findMeetings(Long calendarId, int year, int month, int date) {
    // 특정 날짜 패턴 생성
    String targetDate = String.format("%d-%02d-%02d", year, month, date);

    String query = "SELECT m FROM Meeting m " +
            "JOIN FETCH m.calendar c " +
            "WHERE c.id = :calendarId " +
            "AND (m.meetDTstart LIKE :targetDate% " +                  //시작일이 해당 날짜인 경우
            "OR m.meetDTend LIKE :targetDate% " +                      //종료일이 해당 날짜인 경우
            "OR (m.meetDTstart <= :targetDate AND m.meetDTend >= :targetDate))"; //기간이 해당 날짜를 포함하는 경우

    List<Meeting> meetingList = em.createQuery(query, Meeting.class)
            .setParameter("calendarId", calendarId)
            .setParameter("targetDate", targetDate)
            .getResultList();

    return CalendarViewResponseDto.builder()
            .month(month)
            .year(year)
            .meetingList(meetingList)
            .build();
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

    return CalendarViewResponseDto.builder()
            .meetingList(meetingList)
            .build();
  }
}
