package timetogether.calendar.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timetogether.groupMeeting.MeetType;
import timetogether.calendar.exception.CalendarValidateFail;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CalendarCreateResponseDto {

  private String meetTitle;
  private String meetContent;
  private MeetType meetType;
  private LocalDateTime meetDTstart;
  private LocalDateTime meetDTend;
  private String groupName;
  private String locationName;
  private String locationUrl;

  @Builder
  public CalendarCreateResponseDto(String meetTitle, String meetContent, MeetType meetType, LocalDateTime meetDTstart, LocalDateTime meetDTend, String groupName, String locationName, String locationUrl) throws CalendarValidateFail {
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
