package timetogether.global.response;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import timetogether.domain.calendar.exception.CalendarNotExist;
import timetogether.domain.calendar.exception.CalendarValidateFail;

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
}
