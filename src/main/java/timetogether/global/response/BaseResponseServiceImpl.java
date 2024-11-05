package timetogether.global.response;

import org.springframework.stereotype.Component;

@Component
public class BaseResponseServiceImpl implements BaseResponseService {
  /**
   * 성공 응답 메서드 - 전달 데이터 O
   *
   * @param data - 결과 데이터
   * @param <T>  - 반환 타입 => Generic
   * @return - 응답 객체
   */
  @Override
  public <T> BaseResponse<Object> getSuccessResponse(T data) {
    return BaseResponse.builder()
            .httpStatus(BaseResponseStatus.SUCCESS.getHttpStatus())
            .message(BaseResponseStatus.SUCCESS.getMessage())
            .data(data)
            .build();
  }

  /**
   * 성공 응답 메서드 - 전달 데이터 x
   *
   * @param <T> - 반환 타입 => Generic
   * @return BaseResponse - 응답 객체
   */
  @Override
  public <T> BaseResponse<Object> getSuccessResponse() {
    return BaseResponse.builder()
            .httpStatus(BaseResponseStatus.SUCCESS.getHttpStatus())
            .message(BaseResponseStatus.SUCCESS.getMessage())
            .build();
  }

  /**
   * 실패 응답 메서드
   *
   * @param status - BaseResponseStatus에서 생성한 status
   * @param <T>    - 반환 타입 => Generic
   * @return BaseResponse - 응답 객체
   */
  public <T> BaseResponse<Object> getFailureResponse(BaseResponseStatus status) {
    return BaseResponse.builder()
            .httpStatus(status.getHttpStatus())
            .message(status.getMessage())
            .build();
  }
}