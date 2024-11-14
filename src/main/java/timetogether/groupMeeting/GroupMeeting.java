package timetogether.groupMeeting;

import jakarta.persistence.*;
import lombok.Getter;
import timetogether.group.Group;
import timetogether.oauth2.entity.User;

import java.util.List;

@Entity
@Getter
public class GroupMeeting {
  @Column(name = "groupMeet_id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long groupMeetId;

  private String groupMeetingTitle;

  private MeetType type;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "group_id")
  private Group group;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;



}
