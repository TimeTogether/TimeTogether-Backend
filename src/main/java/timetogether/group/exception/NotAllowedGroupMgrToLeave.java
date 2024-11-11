package timetogether.group.exception;

import lombok.AllArgsConstructor;
import timetogether.global.response.BaseException;
import timetogether.global.response.BaseResponseStatus;

@AllArgsConstructor
public class NotAllowedGroupMgrToLeave extends BaseException {
  public NotAllowedGroupMgrToLeave(BaseResponseStatus baseResponseStatus) {
  }
}
