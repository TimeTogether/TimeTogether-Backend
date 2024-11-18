package timetogether.calendar;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timetogether.meeting.Meeting;
import timetogether.oauth2.entity.User;

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

  @OneToOne(mappedBy = "calendar")
  private User user;


  @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL)
  private List<Meeting> meetings = new ArrayList<>();

  public void setUser(User user) {
    this.user = user;
    if (user.getCalendar() != this) {
      user.updateCalendarId(this);
    }
  }

  public Calendar(User user) {
    this.user = user;
    this.meetings = new ArrayList<>();
  }

}