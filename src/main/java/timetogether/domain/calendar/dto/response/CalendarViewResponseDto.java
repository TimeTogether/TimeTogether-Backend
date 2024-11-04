package timetogether.domain.calendar.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timetogether.domain.Meeting;

import java.util.List;

@Getter
@NoArgsConstructor
public class CalendarViewResponseDto {
  private int year;
  private int month;
  private int date = -1;//default
  private List<Meeting> meetingList;

  @Builder
  public CalendarViewResponseDto(int month, int year, List<Meeting> meetingList) { //미팅들 반환용
    this.year = year;
    this.month = month;
    this.meetingList = meetingList;
  }
  @Builder
  public CalendarViewResponseDto(int month, int year, int date, List<Meeting> meetingList) { //미팅들 반환용
    this.year = year;
    this.month = month;
    this.date =  date;
    this.meetingList = meetingList;
  }
  @Builder
  public CalendarViewResponseDto(List<Meeting> meetingList) { //미팅 반환용
    this.meetingList = meetingList;
  }
}