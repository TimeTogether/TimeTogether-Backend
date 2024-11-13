package timetogether.group.exception;

import lombok.AllArgsConstructor;
import timetogether.global.response.BaseException;
import timetogether.global.response.BaseResponseStatus;

@AllArgsConstructor
public class GroupNotFoundOrNotMgrException extends BaseException{
  public GroupNotFoundOrNotMgrException(BaseResponseStatus status) {
    super(status);
  }

}