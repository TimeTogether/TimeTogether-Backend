package timetogether.oauth2.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import timetogether.calendar.Calendar;
import timetogether.group.Group;
import timetogether.groupMeeting.GroupMeeting;
import timetogether.when2meet.When2meet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Slf4j
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

  @ManyToMany
  @JoinTable(
          name = "user_group",  // 중간 테이블 이름
          joinColumns = @JoinColumn(name = "social_id"),  // User 쪽 외래키
          inverseJoinColumns = @JoinColumn(name = "group_id")  // Group 쪽 외래키
  )
  private List<Group> groupList;

  @Enumerated(EnumType.STRING)
  private Role role;

  @Enumerated(EnumType.STRING)
  private SocialType socialType; // KAKAO, NAVER, GOOGLE

  private String refreshToken; // 리프레시 토큰

//  @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE, orphanRemoval = true)
//  private List<When2meet> when2meetList = new ArrayList<>();
  
  /*
   * 그룹 내에서 유저를 삭제하기 위한 equals 재정의
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equals(getSocialId(), user.getSocialId()) && Objects.equals(getUserName(), user.getUserName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getSocialId(), getUserName());
  }

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

  public void initGroupMeeting(GroupMeeting groupMeeting) {
    groupMeetingList.add(groupMeeting);
    groupMeeting.setUser(this);
  }
  /*
   * 유저내에서 그룹을 삭제하기 위한 내부 로직
   */
  public void removeGroupFromUser(Group group) {
    int index = groupList.indexOf(group);
    groupList.remove(index);
    log.info("groupList : {}" , groupList);
  }
  /*
   * 유저내에서 그룹에 등록하는 내부 로직
   */
  public void addSocailIdGroup(Group group){
    groupList.add(group);
    log.info("groupList: {}", groupList);
  }
  @Builder
  public User(String socialId, String userName, SocialType socialType, Role role) {
    this.socialId = socialId; // social Id 고유 식별자
    this.userName = userName;
    this.socialType = socialType;
    this.role = role;
  }
}
