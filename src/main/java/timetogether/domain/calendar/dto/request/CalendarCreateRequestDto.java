package timetogether.domain.calendar.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timetogether.domain.MeetType;
import timetogether.domain.calendar.dto.response.CalendarCreateResponseDto;
import timetogether.domain.calendar.exception.CalendarValidateFail;
import timetogether.global.response.BaseResponseStatus;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CalendarCreateRequestDto {

  private String meetTitle;
  private String meetContent;
  private MeetType meetType;
  private LocalDateTime meetDTstart;
  private LocalDateTime meetDTend;
  private String groupName;
  private String locationName;
  private String locationUrl;

  @Builder
  public CalendarCreateRequestDto(String meetTitle, String meetContent, MeetType meetType, LocalDateTime meetDTstart, LocalDateTime meetDTend, String groupName, String locationName, String locationUrl) throws CalendarValidateFail {
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
