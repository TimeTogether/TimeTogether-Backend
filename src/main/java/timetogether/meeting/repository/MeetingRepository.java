package timetogether.meeting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import timetogether.meeting.Meeting;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long>{
  Optional<List<Meeting>> findByGroupName(String groupName);
  Optional<Meeting> findById(Long id);

}