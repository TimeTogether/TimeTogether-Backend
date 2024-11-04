package timetogether.global.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class BaseResponse<T> {
  private String message; //메시지
  private HttpStatus httpStatus;
  private T data; //전달 데이터

  @Builder
  public BaseResponse(String message, HttpStatus httpStatus, T data) {
    this.message = message;
    this.httpStatus = httpStatus;
    this.data = data;
  }
  @Builder
  public BaseResponse(String message, HttpStatus httpStatus) {
    this.message = message;
    this.httpStatus = httpStatus;
    this.data = null;
  }
}
