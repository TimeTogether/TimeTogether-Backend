package timetogether.calendar;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timetogether.meeting.Meeting;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "calendar")
public class Calendar {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "calendar_id")
  private Long calendarId;


  @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL)
  private List<Meeting> meetings = new ArrayList<>();

}