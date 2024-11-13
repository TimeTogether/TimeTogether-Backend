package timetogether.ranktime;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timetogether.group.Group;
import timetogether.oauth2.entity.User;
import timetogether.when2meet.When2meet;

@Entity
@Getter
@Table(name = "rank_table")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RankTime {
  @Column(name = "time_id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "rank_value")
  private String rank;
  @Column(name = "time_value")
  private String time;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "date")
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