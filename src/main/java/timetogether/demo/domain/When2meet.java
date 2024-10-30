package timetogether.demo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timetogether.oauth2.entity.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class When2meet {

  @Id
  private String date;

  private String day;

  private MeetType type;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "group_id")
  private Group group;

  @Builder
  public When2meet(String date, String day, MeetType type, User user, Group group) {
    this.date = date;
    this.day = day;
    this.type = type;
    this.user = user;
    this.group = group;
  }
}