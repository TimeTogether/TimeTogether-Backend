package timetogether.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timetogether.domain.calendar.Calendar;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Meeting {

  @Column(name = "meet_id")
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column
  private String meetDTstart; //2024-07-04 형식

  @NotNull
  @Column
  private String meetDTend;

  private MeetType meetType;

  private String meetTitle;

  private String meetContent;

  @NotNull
  private String groupName;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "calendar_id")
  private Calendar calendar;

  @OneToOne(fetch = FetchType.LAZY)
  private Where2meet where2meet;

  @Builder
  public Meeting(String meetDTstart, String meetDTend, MeetType meetType, String meetTitle, String meetContent, String groupName, Calendar calendar, Where2meet where2meet) {
    this.meetDTstart = meetDTstart;
    this.meetDTend = meetDTend;
    this.meetType = meetType;
    this.meetTitle = meetTitle;
    this.meetContent = meetContent;
    this.groupName = groupName;
    this.calendar = calendar;
    this.where2meet = where2meet;
  }
}
