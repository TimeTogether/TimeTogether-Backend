package timetogether.domain.meeting;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timetogether.domain.MeetType;
import timetogether.domain.where2meet.Where2meet;
import timetogether.domain.calendar.Calendar;

import java.time.LocalDateTime;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Meeting {

  @Column(name = "meet_id")
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private LocalDateTime meetDTstart;

  @NotNull
  private LocalDateTime meetDTend;

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
  public Meeting(LocalDateTime meetDTstart, LocalDateTime meetDTend, MeetType meetType, String meetTitle, String meetContent, String groupName, Calendar calendar, Where2meet where2meet) {
    this.meetDTstart = meetDTstart;
    this.meetDTend = meetDTend;
    this.meetType = meetType;
    this.meetTitle = meetTitle;
    this.meetContent = meetContent;
    this.groupName = groupName;
    this.calendar = calendar;
    this.where2meet = where2meet;
    //where2meet을 저장하는 비즈니스 로직 필요

  }
}
