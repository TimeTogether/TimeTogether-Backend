package timetogether.domain.calendar.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timetogether.domain.MeetType;
import timetogether.domain.Meeting;

import java.util.List;

@Getter
@NoArgsConstructor
public class CalendarCreateRequestDto {

  private String meetTitle;
  private String meetContent;
  private MeetType meetType;
  private String meetDTstart;
  private String meetDTend;
  private String locationName;
  private String locationUrl;

  @Builder
  public CalendarCreateRequestDto(String meetTitle, String meetContent, MeetType meetType, String meetDTend, String locationName, String locationUrl) {
    this.meetTitle = meetTitle;
    this.meetContent = meetContent;
    this.meetType = meetType;
    this.meetDTend = meetDTend;
    this.locationName = locationName;
    this.locationUrl = locationUrl;
    //meetDTstart는 service단에서 정의
  }
}
