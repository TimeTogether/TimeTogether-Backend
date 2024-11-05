package timetogether.meeting.dto.response;

import lombok.Builder;
import lombok.Getter;
import timetogether.meeting.MeetType;
import timetogether.calendar.dto.response.CalendarSimpleDto;
import timetogether.meeting.Meeting;

import java.time.LocalDateTime;

@Getter
@Builder
public class MeetingResponseDto {
  private Long id;
  private LocalDateTime meetDTstart;
  private LocalDateTime meetDTend;
  private MeetType meetType;
  private String meetTitle;
  private String meetContent;
  private String groupName;
  private CalendarSimpleDto calendar;  // Calendar의 간단한 정보만 포함하는 DTO

  public static MeetingResponseDto from(Meeting meeting) {
    return MeetingResponseDto.builder()
            .id(meeting.getId())
            .meetDTstart(meeting.getMeetDTstart())
            .meetDTend(meeting.getMeetDTend())
            .meetType(meeting.getMeetType())
            .meetTitle(meeting.getMeetTitle())
            .meetContent(meeting.getMeetContent())
            .groupName(meeting.getGroupName())
            .calendar(CalendarSimpleDto.from(meeting.getCalendar()))
            .build();
  }
}
