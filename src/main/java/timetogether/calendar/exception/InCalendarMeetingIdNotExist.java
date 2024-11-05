package timetogether.calendar.exception;

import lombok.AllArgsConstructor;
import timetogether.global.response.BaseException;
import timetogether.global.response.BaseResponseStatus;

@AllArgsConstructor
public class InCalendarMeetingIdNotExist extends BaseException {
  public InCalendarMeetingIdNotExist(BaseResponseStatus status) {
    super(status);
  }
}
