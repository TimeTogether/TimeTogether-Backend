package timetogether.calendar.exception;

import lombok.AllArgsConstructor;
import timetogether.global.response.BaseException;
import timetogether.global.response.BaseResponseStatus;

@AllArgsConstructor
public class CalendarNotExist extends BaseException {
  public CalendarNotExist(BaseResponseStatus status) {
    super(status);
  }
}
