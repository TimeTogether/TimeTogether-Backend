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
public class When2meet {

  @Column(name = "when2meet_id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long when2meetId;

  private String day;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "group_id")
  private Group group;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name ="groupMeet_id")
  private GroupMeeting groupMeeting;

  @Builder
  public When2meet(String date, String day, MeetType type, User user, Group group) {
    this.date = date;
    this.day = day;
    this.type = type;
    this.user = user;
    this.group = group;
  }
}