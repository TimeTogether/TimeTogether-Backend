package timetogether.calendar.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timetogether.groupMeeting.MeetType;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CalendarUpdateRequestDto {

  private String meetTitle;
  private String meetContent;
  private MeetType meetType;
  private LocalDateTime meetDTstart;
  private LocalDateTime meetDTend;
  private String groupName;
  private String locationName;
  private String locationUrl;

  @Builder
  public CalendarUpdateRequestDto(String meetTitle, String meetContent, MeetType meetType, LocalDateTime meetDTstart, LocalDateTime meetDTend, String groupName, String locationName, String locationUrl) {
    this.meetTitle = meetTitle;
    this.meetContent = meetContent;
    this.meetType = meetType;
    this.meetDTstart = meetDTstart;
    this.meetDTend = meetDTend;
    this.groupName = groupName;
    this.locationName = locationName;
    this.locationUrl = locationUrl;
  }
}