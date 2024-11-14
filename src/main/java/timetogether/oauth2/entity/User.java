package timetogether.oauth2.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import timetogether.calendar.Calendar;
import timetogether.groupMeeting.GroupMeeting;
import timetogether.when2meet.When2meet;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

  @Column(name = "social_id", unique = true)
  @Id
  private String socialId; // 로그인한 소셜 타입의 식별자 값 (일반 로그인인 경우 null)

  @NotNull
  private String userName;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "calendar_id")
  private Calendar calendar;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "groupMeet_id")
  private GroupMeeting groupMeeting;

  @OneToMany(mappedBy = "user")
  private List<GroupMeeting> groupMeetingList = new ArrayList<>();

  @Enumerated(EnumType.STRING)
  private Role role;

  @Enumerated(EnumType.STRING)
  private SocialType socialType; // KAKAO, NAVER, GOOGLE

  private String refreshToken; // 리프레시 토큰

  @OneToMany(mappedBy = "group",cascade = CascadeType.REMOVE, orphanRemoval = true)
  private List<When2meet> when2meetList = new ArrayList<>();

  // 유저 권한 설정 메소드
  public void authorizeUser() {
    this.role = Role.USER;
  }

  public void updateRefreshToken(String updateRefreshToken) {
    this.refreshToken = updateRefreshToken;
  }

  public void updateCalendarId(Calendar calendar) {
    this.calendar = calendar;
  }

  public void addWhen2meet(When2meet when2meet) {
    this.when2meetList.add(when2meet);
    when2meet.setUser(this);  // 양방향 연관관계 설정
  }

}
