package timetogether.oauth2.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import timetogether.demo.domain.Calendar;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

  @Column(name = "social_id")
  @Id
  private String socialId; // 로그인한 소셜 타입의 식별자 값 (일반 로그인인 경우 null)

  @NotNull
  @Column(unique = true)
  private String userName;

  @NotNull
  private boolean groupMgr;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "calendar_id")
  private Calendar calendar;

  @Enumerated(EnumType.STRING)
  private Role role;

  @Enumerated(EnumType.STRING)
  private SocialType socialType; // KAKAO, NAVER, GOOGLE

  private String refreshToken; // 리프레시 토큰

  // 유저 권한 설정 메소드
  public void authorizeUser() {
    this.role = Role.USER;
  }

  public void updateRefreshToken(String updateRefreshToken) {
    this.refreshToken = updateRefreshToken;
  }

  @Builder
  public User(String socialId, boolean groupMgr, String userName, SocialType socialType, Role role) {
    this.socialId = socialId; // social Id 고유 식별자
    this.userName = userName;
    this.socialType = socialType;
    this.role = role;
    this.groupMgr = groupMgr;
  }
}
