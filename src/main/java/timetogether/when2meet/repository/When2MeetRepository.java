package timetogether.when2meet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import timetogether.calendar.Calendar;
import timetogether.when2meet.When2meet;

import java.util.Optional;

public interface When2MeetRepository extends JpaRepository<When2meet, String> {

}
