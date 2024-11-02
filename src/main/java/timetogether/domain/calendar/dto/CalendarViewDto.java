package timetogether.domain.calendar.dto;

import lombok.Builder;
import lombok.Getter;
import timetogether.domain.Meeting;

import java.util.List;

@Getter
public class CalendarViewDto {
  private int year;
  private int month;
  private int date;
  private List<Meeting> meetingList;
  private Meeting meeting;

  @Builder
  public CalendarViewDto(int month, int year, int date, List<Meeting> meetingList) { //미팅들 반환용
    this.year = year;
    this.month = month;
    this.date =  date;
    this.meetingList = meetingList;
  }
  @Builder
  public CalendarViewDto(int month, int year, int date, Meeting meeting) {//미팅 한개 반환용
    this.year = year;
    this.month = month;
    this.date =  date;
    this.meeting = meeting;
  }
}