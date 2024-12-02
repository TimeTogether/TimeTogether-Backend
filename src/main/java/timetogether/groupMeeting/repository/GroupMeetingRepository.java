package timetogether.groupMeeting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import timetogether.group.Group;
import timetogether.groupMeeting.GroupMeeting;
import timetogether.meeting.Meeting;
import timetogether.oauth2.entity.User;

import java.util.List;
import java.util.Optional;

public interface GroupMeetingRepository extends JpaRepository<GroupMeeting, Long> {
    @Override
    Optional<GroupMeeting> findById(Long groupMeetId);

//    List<GroupMeeting> findByGroupAndGroupMeetingTitleAndUser(Group group, String groupMeetingTitle, User user);
    @Query("SELECT gm FROM GroupMeeting gm WHERE gm.group = :group AND gm.groupMeetingTitle = :groupMeetingTitle AND gm.user = :user")
    GroupMeeting findByGroupAndGroupMeetingTitleAndUser(@Param("group") Group group,
                                          @Param("groupMeetingTitle") String groupMeetingTitle,
                                          @Param("user") User user);
    GroupMeeting findByGroupAndGroupMeetingTitle(Group group, String groupMeetingTitle);
    List<GroupMeeting> findByGroupMeetingTitle(String groupMeetingTitle);

    List<GroupMeeting> findByGroupId(Long groupId);
}
