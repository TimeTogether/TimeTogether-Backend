package timetogether.when2meet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import timetogether.groupMeeting.GroupMeeting;
import timetogether.groupMeeting.MeetType;
import timetogether.oauth2.entity.User;
import timetogether.when2meet.When2meet;

import java.util.Dictionary;
import java.util.List;
import java.util.Optional;

@Repository
public interface When2MeetRepository extends JpaRepository<When2meet, String> {
    Optional<When2meet> findByDate(String date);
    Optional<When2meet> findByDateAndUserAndTypeAndGroupMeeting(String date, User user, MeetType type, GroupMeeting groupMeeting);
    List<When2meet> findByUserAndTypeAndGroupMeeting(User user, MeetType type, GroupMeeting groupMeeting);
}
