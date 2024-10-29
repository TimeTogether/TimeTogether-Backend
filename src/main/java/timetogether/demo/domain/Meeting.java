package timetogether.demo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Meeting {

  @Column(name = "meet_id")
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String meetDTstart;

  @Column(nullable = false, unique = true)
  private String meetDTend;

  private MeetType meetType;

  private String meetTitle;

  private String meetContent;

  private String groupName;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "calendar_id")
  private Calendar calendar;

  @OneToOne(fetch = FetchType.LAZY)
  private Where2meet where2meet;

  @Builder
  public Meeting(String meetDTstart, MeetType meetType, String meetDTend, String meetTitle, String meetContent, String groupName) {
    this.meetDTstart = meetDTstart;
    this.meetDTend = meetDTend;
    this.meetType = meetType;
    this.meetTitle = meetTitle;
    this.meetContent = meetContent;
    this.groupName = groupName;
  }
}
