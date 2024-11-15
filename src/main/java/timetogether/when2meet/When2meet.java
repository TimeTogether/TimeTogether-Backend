package timetogether.when2meet;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timetogether.group.Group;
import timetogether.groupMeeting.GroupMeeting;
import timetogether.groupMeeting.MeetType;
import timetogether.oauth2.entity.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "when2meet")
public class When2meet {

  @Column(name = "when2meet_id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long when2meetId;

  private String date;

  private String day;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "social_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "group_id")
  private Group group;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name ="groupMeet_id")
  private GroupMeeting groupMeeting;

  private MeetType type;

  @Builder
  public When2meet(String date, String day, Group group, GroupMeeting groupMeeting, MeetType type) {
    this.date = date;
    this.day = day;
    this.group = group;
    this.groupMeeting = groupMeeting;
    this.type = type;
  }

}