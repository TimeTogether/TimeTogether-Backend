package timetogether.global.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BaseResponse<T> {
  private String message; //메시지
  private int code; //코드
  private T data; //전달 데이터

  @Builder
  public BaseResponse(String message, int code, T data) {
    this.message = message;
    this.code = code;
    this.data = data;
  }
}
