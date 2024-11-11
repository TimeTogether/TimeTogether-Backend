package timetogether.global.response;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import timetogether.calendar.exception.CalendarNotExist;
import timetogether.calendar.exception.CalendarValidateFail;
import timetogether.group.exception.*;

@RestControllerAdvice
@RequiredArgsConstructor
public class ErrorAdvice {

  private final BaseResponseService baseResponseService;

  @ExceptionHandler(CalendarValidateFail.class)
  public BaseResponse<Object> handleCalendarValidationFailError(CalendarValidateFail e) {
    return baseResponseService.getFailureResponse(
            e.getStatus()
    );
  }

  @ExceptionHandler(CalendarNotExist.class)
  public BaseResponse<Object> handleCalendarNotExistError(CalendarNotExist e){
    return baseResponseService.getFailureResponse(
            e.getStatus()
    );
  }

  @ExceptionHandler(Exception.class)
  public BaseResponse<Object> handleGeneralException() {
    return baseResponseService.getFailureResponse(
            BaseResponseStatus.INTERNAL_SERVER_ERROR
    );
  }

  @ExceptionHandler(GroupNotFoundOrNotMgrException.class)
  public BaseResponse<Object> handleGroupNotFoundOrNotMgr(){
    return baseResponseService.getFailureResponse(
            BaseResponseStatus.NOT_VALID_MGR_OR_GROUPNAME
    );
  }

  @ExceptionHandler(GroupTimesLimitSevenDays.class)
  public BaseResponse<Object> handleGroupTimesLimit(){
    return baseResponseService.getFailureResponse(
      BaseResponseStatus.NOT_VALID_GROUPTIMES
    );
  }

  @ExceptionHandler(NotValidMemberException.class)
  public BaseResponse<Object> handleNotValidMembers(){
    return baseResponseService.getFailureResponse(
            BaseResponseStatus.NOT_VALID_MEMBERS
    );
  }

  @ExceptionHandler(GroupNotFoundException.class)
  public BaseResponse<Object> handleNotFoundGroupWhenEditingGroup(){
    return baseResponseService.getFailureResponse(
            BaseResponseStatus.NOT_EXIST_GROUPID
    );
  }

  @ExceptionHandler(NotGroupMgrInGroup.class)
  public BaseResponse<Object> handleNotValidMgrWhenEditingGroup(){
    return baseResponseService.getFailureResponse(
            BaseResponseStatus.NOT_VALID_MGR
    );
  }

  @ExceptionHandler(NotAllowedGroupMgrToLeave.class)
  public BaseResponse<Object> handleNotAllowedGroupMgrLeaving(){
    return baseResponseService.getFailureResponse(
            BaseResponseStatus.NOT_ALLOWED_TO_LEAVE_FOR_GROUPMGR
    );
  }

}
