package timetogether.domain.meeting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import timetogether.domain.meeting.Meeting;

import java.util.Optional;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long>{
  Optional<Meeting> findByGroupName(String groupName);
  Optional<Meeting> findById(Long id);

}