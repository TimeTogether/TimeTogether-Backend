package timetogether.domain.calendar.exception;

import lombok.AllArgsConstructor;
import timetogether.domain.calendar.dto.request.CalendarCreateRequestDto;
import timetogether.global.response.BaseException;
import timetogether.global.response.BaseResponseStatus;


public class CalendarValidateFail extends BaseException { ;
  public CalendarValidateFail(BaseResponseStatus baseResponseStatus) {
    super(baseResponseStatus);
  }

}
