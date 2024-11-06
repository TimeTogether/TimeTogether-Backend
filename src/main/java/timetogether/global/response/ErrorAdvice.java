package timetogether.global.response;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import timetogether.calendar.exception.CalendarNotExist;
import timetogether.calendar.exception.CalendarValidateFail;
import timetogether.group.exception.NotValidMembersException;

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

  @ExceptionHandler(NotValidMembersException.class)
  public BaseResponse<Object> handleNotValidMembersWhenCreatingGroup(){
    return baseResponseService.getFailureResponse(
            BaseResponseStatus.NOT_VALID_MEMBERS_WHEN_CREATING_GROUP
    );
  }
}
