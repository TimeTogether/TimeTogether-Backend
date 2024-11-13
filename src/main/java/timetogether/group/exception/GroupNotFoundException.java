package timetogether.group.exception;

import lombok.AllArgsConstructor;
import timetogether.global.response.BaseException;
import timetogether.global.response.BaseResponseStatus;

@AllArgsConstructor
public class GroupNotFoundException extends BaseException {
  public GroupNotFoundException(BaseResponseStatus status) {
    super(status);
  }
}
