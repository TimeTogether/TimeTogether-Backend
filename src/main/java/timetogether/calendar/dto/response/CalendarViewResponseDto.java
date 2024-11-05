package timetogether.calendar.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timetogether.meeting.Meeting;
import timetogether.meeting.dto.response.MeetingResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class CalendarViewResponseDto {
  private int year;
  private int month;
  private int date = -1;//default
  private List<MeetingResponseDto> meetingList;
  @Builder
  public CalendarViewResponseDto(int year, int month, List<MeetingResponseDto> meetingList) {
    this.year = year;
    this.month = month;
    this.meetingList = meetingList;
  }

  public static CalendarViewResponseDto of(int year, int month, List<Meeting> meetings) {
    return CalendarViewResponseDto.builder()
            .year(year)
            .month(month)
            .meetingList(meetings.stream()
                    .map(MeetingResponseDto::from)
                    .collect(Collectors.toList()))
            .build();
  }
}