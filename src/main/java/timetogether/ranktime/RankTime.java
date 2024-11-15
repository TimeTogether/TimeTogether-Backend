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
@Table(name = "rank")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RankTime {

  private static final String ZERO = "0";

  @Column(name = "rankTime_id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long rankTimeId;

  @Column(name = "rank_value")
  private String rank;
  @Column(name = "time_value")
  private String time;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "when2meet_id")
  private When2meet when2meet;

  @Builder
  public RankTime(int rank, int time, When2meet when2meet) {
    this.rank = parserRank(rank);
    this.time = parserTime(rank);
    this.when2meet = when2meet;
  }

  private String parserTime(int time) {
    return ZERO.repeat(time);
  }

  private String parserRank(int rank) {
    return ZERO.repeat(rank);
  }
}