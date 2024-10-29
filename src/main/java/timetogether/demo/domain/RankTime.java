package timetogether.demo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RankTime {
  @Column(name = "time_id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String rank;
  private String time;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "date_id")
  private When2meet when2meet;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "group_id")
  private Group group;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Builder
  public RankTime(When2meet when2meet, Group group, User user, String rank, String time) {
    this.when2meet = when2meet;
    this.group = group;
    this.user = user;
    this.rank = rank;
    this.time = time;
  }
}