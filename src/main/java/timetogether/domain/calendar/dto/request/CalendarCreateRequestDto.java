package timetogether.domain.calendar.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timetogether.domain.MeetType;

@Getter
@NoArgsConstructor
public class CalendarCreateRequestDto {

  private String meetTitle;
  private String meetContent;
  private MeetType meetType;
  private String meetDTend;
  private String groupName;
  private String locationName;
  private String locationUrl;

  @Builder
  public CalendarCreateRequestDto(String meetTitle, String meetContent, MeetType meetType, String meetDTend, String groupName, String locationName, String locationUrl) {
    this.meetTitle = meetTitle;
    this.meetContent = meetContent;
    this.meetType = meetType;
    this.meetDTend = meetDTend;
    this.groupName = groupName;
    this.locationName = locationName;
    this.locationUrl = locationUrl;
    //Meeting 클래스의 meetDTstart 값은 service단에서 정의
  }
}
