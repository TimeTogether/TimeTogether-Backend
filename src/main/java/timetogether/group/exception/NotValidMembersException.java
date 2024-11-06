package timetogether.group.exception;

import lombok.AllArgsConstructor;
import timetogether.global.response.BaseException;
import timetogether.global.response.BaseResponseStatus;

@AllArgsConstructor
public class NotValidMembersException extends BaseException{
  public NotValidMembersException(BaseResponseStatus status) {
    super(status);
  }
}