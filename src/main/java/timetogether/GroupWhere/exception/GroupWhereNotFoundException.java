package timetogether.GroupWhere.exception;

import timetogether.global.response.BaseException;
import timetogether.global.response.BaseResponseStatus;

public class GroupWhereNotFoundException  extends BaseException {
  public GroupWhereNotFoundException(BaseResponseStatus status) {
    super(status);
  }
}