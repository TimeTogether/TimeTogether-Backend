package timetogether.groupMeeting;

import org.springframework.data.jpa.repository.JpaRepository;
import timetogether.group.Group;
import timetogether.oauth2.entity.User;

import java.util.List;
import java.util.Optional;

public interface GroupMeetingRepository extends JpaRepository<GroupMeeting, Long> {
    @Override
    Optional<GroupMeeting> findById(Long groupMeetId);

    GroupMeeting findByGroupAndGroupMeetingTitleAndUser(Group group, String groupMeetingTitle, User user);
    List<GroupMeeting> findByGroupMeetingTitle(String groupMeetingTitle);
}
