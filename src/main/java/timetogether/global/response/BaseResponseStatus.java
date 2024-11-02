package timetogether.global.response;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {
  // -------- 성공 코드 시작 -------- //
  SUCCESS(100, "요청에 성공했습니다."),
  // -------- 성공 코드 종료 -------- //

  // -------- 실패 코드 시작 -------- //
  // -------- 필요한 에러 코드 추가 => code 만들 때 안겹치게 몇번대 쓸지 적기 -------- //
  /**
   * 서버 내부 오류
   * Code: 200번대
   */
  INTERNAL_SERVER_ERROR(200, "서버 내부 오류입니다."),
  /**
   * Calendar
   * Code: 300번대
   */
  NOT_FOUND_MEETINGS_IN_CALENDAR(300, "등록된 일정이 없습니다."),
  /**
   * User
   * Code: 400번대
   */
  NOT_VALID_USER(400, "유효한 유저가 아닙니다.");
  // -------- 실패 코드 종료 -------- //
  private int code; //코드
  private String message; //메시지

  BaseResponseStatus(int code, String message) {
    this.code = code;
    this.message = message;
  }
}
