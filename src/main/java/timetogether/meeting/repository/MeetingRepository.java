package timetogether.meeting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import timetogether.meeting.Meeting;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long>{
  List<Meeting> findByGroupName(String groupName);
  @Query(value = "SELECT * FROM meeting m " +
          "WHERE m.calendar_id = :calendarId " +
          "AND (DATE(:date) >= DATE(m.meetDTstart) AND DATE(:date) <= DATE(m.meetDTend))",
          nativeQuery = true)
  List<Meeting> findByCalendarAndDate(@Param("calendarId") Long calendarId,
                                      @Param("date") String date);
  @Query(value = "SELECT * FROM meeting m " +
          "WHERE m.calendar_id = :calendarId " +
          "AND DATE(m.meetDTstart) >= DATE(:date)",
          nativeQuery = true)
  List<Meeting> findUpcomingMeetings(@Param("calendarId") Long calendarId,
                                     @Param("date") LocalDateTime date);

  @Query(value = "SELECT * FROM meeting m " +
          "WHERE m.calendar_id = :calendarId " +
          "AND DATE(m.meetDTend) < DATE(:date)",
          nativeQuery = true)
  List<Meeting> findPastMeetings(@Param("calendarId") Long calendarId,
                                 @Param("date") LocalDateTime date);


}