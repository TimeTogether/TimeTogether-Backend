package timetogether.group.exception;

import lombok.AllArgsConstructor;
import timetogether.global.response.BaseException;
import timetogether.global.response.BaseResponseStatus;

@AllArgsConstructor
public class NotValidMemberException extends BaseException{
  public NotValidMemberException(BaseResponseStatus status) {
    super(status);
  }
}