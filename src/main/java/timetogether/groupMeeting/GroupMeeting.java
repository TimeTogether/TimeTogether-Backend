package timetogether.groupMeeting;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timetogether.GroupWhere.GroupWhere;
import timetogether.group.Group;
import timetogether.oauth2.entity.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "group_meeting")
public class GroupMeeting {
  @Column(name = "groupMeet_id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long groupMeetId;

  private String groupMeetingTitle;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "group_id")
  private Group group;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @OneToMany(mappedBy = "groupMeeting", cascade = CascadeType.REMOVE, orphanRemoval = true)
  private List<GroupWhere> groupWhereList = new ArrayList<>();

  @Builder
  public GroupMeeting(String groupMeetingTitle, Group group, User user) {
    this.groupMeetingTitle = groupMeetingTitle;
    this.group = group;
    this.user = user;
  }


  public void setUser(User user) {
    this.user = user;
  }
}
