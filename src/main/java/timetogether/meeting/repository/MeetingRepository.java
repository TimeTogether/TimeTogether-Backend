package timetogether.meeting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import timetogether.meeting.Meeting;

import java.util.Optional;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long>{
  Optional<Meeting> findByGroupName(String groupName);
  Optional<Meeting> findById(Long id);

}