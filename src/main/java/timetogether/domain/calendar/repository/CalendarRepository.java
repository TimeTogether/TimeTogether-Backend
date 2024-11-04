package timetogether.domain.calendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import timetogether.domain.calendar.Calendar;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long>, CalendarRepositoryCustom{


}
