package timetogether.calendar.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CalendarMergeResponseDto {
  private CalendarViewResponseDto e;
  private List<CalendarWhere2meetDto> l;

  @Builder
  public CalendarMergeResponseDto(CalendarViewResponseDto e, List<CalendarWhere2meetDto> l) {
    this.e = e;
    this.l = l;
  }
}
