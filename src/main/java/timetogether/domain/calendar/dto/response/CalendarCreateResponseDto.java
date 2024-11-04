package timetogether.domain.calendar.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timetogether.domain.MeetType;

@Getter
@NoArgsConstructor
public class CalendarCreateResponseDto {

  private String meetTitle;
  private String meetContent;
  private MeetType meetType;
  private String meetDTstart;
  private String meetDTend;
  private String locationName;
  private String locationUrl;

  @Builder
  public CalendarCreateResponseDto(String meetTitle, String meetContent, MeetType meetType, String meetDTstart, String meetDTend, String locationName, String locationUrl) {
    this.meetTitle = meetTitle;
    this.meetContent = meetContent;
    this.meetType = meetType;
    this.meetDTstart = meetDTstart;
    this.meetDTend = meetDTend;
    this.locationName = locationName;
    this.locationUrl = locationUrl;
  }
}
