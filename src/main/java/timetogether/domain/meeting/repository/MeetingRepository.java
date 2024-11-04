package timetogether.domain.meeting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import timetogether.domain.calendar.repository.CalendarRepositoryCustom;
import timetogether.domain.meeting.Meeting;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long>{


}