package timetogether.domain.meeting;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timetogether.domain.MeetType;
import timetogether.domain.calendar.dto.request.CalendarUpdateRequestDto;
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

  @NotNull
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

  }

  //updateDto를 Meeting으로 변환하는 메소드
  public void update(CalendarUpdateRequestDto request, Where2meet newWhere) {
    this.meetTitle = request.getMeetTitle();
    this.meetContent = request.getMeetContent();
    this.meetType = request.getMeetType();
    this.meetDTstart = request.getMeetDTstart();
    this.meetDTend = request.getMeetDTend();
    this.where2meet = newWhere;
  }
}
