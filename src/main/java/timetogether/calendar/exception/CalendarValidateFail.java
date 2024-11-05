package timetogether.calendar.exception;

import timetogether.global.response.BaseException;
import timetogether.global.response.BaseResponseStatus;


public class CalendarValidateFail extends BaseException { ;
  public CalendarValidateFail(BaseResponseStatus baseResponseStatus) {
    super(baseResponseStatus);
  }

}
