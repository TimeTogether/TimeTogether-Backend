package timetogether.group.exception;

import lombok.AllArgsConstructor;
import timetogether.global.response.BaseException;
import timetogether.global.response.BaseResponseStatus;

@AllArgsConstructor
public class NotGroupMgrInGroup extends BaseException {
  public NotGroupMgrInGroup(BaseResponseStatus status) {
    super(status);
  }

}
