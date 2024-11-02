package timetogether.domain.calendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import timetogether.domain.calendar.Calendar;
import timetogether.domain.calendar.dto.CalendarViewDto;

import java.util.List;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {

}
