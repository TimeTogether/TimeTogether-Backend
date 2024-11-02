package timetogether.global.response;

import org.springframework.http.HttpStatus;

public abstract class BaseException extends RuntimeException {
  public abstract BaseResponseStatus getStatus();
  public abstract HttpStatus getHttpStatus();
  public abstract String getMessage();
}
