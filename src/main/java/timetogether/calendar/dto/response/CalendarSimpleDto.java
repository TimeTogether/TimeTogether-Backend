package timetogether.calendar.dto.response;

import lombok.Builder;
import lombok.Getter;
import timetogether.calendar.Calendar;

@Getter
@Builder
public class CalendarSimpleDto {
  private Long id;

  public static CalendarSimpleDto from(Calendar calendar) {
    return CalendarSimpleDto.builder()
            .id(calendar.getId())
            .build();
  }
}