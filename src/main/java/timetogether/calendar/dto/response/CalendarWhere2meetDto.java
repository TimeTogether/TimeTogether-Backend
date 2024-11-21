package timetogether.calendar.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CalendarWhere2meetDto {
  private Long meetingId;
  private String locationName;
  private String locationUrl;

  @Builder
  public CalendarWhere2meetDto(Long meetingId, String locationName, String locationUrl) {
    this.meetingId = meetingId;
    this.locationName = locationName;
    this.locationUrl = locationUrl;
  }
}
