package timetogether.group.exception;

import timetogether.global.response.BaseException;
import timetogether.global.response.BaseResponseStatus;

public class GroupTimesLimitSevenDays extends BaseException {
  public GroupTimesLimitSevenDays(BaseResponseStatus status) {
    super(status);
  }
}