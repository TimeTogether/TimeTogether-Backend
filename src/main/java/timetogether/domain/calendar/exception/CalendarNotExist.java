package timetogether.domain.calendar.exception;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import timetogether.global.response.BaseException;
import timetogether.global.response.BaseResponseStatus;

@NoArgsConstructor
public class CalendarNotExist extends BaseException {

  @Override
  public BaseResponseStatus getStatus() {
    return BaseResponseStatus.NOT_VALID_USER;
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.NOT_FOUND;
  }

  @Override
  public String getMessage() {
    return BaseResponseStatus.NOT_VALID_USER.getMessage();
  }
}
