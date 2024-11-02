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
   * Calendar
   * Code: 200번대
   */
  NOT_FOUND_CALENDAR_(201, "캘린더가 존재하지 않습니다.");
  // -------- 실패 코드 종료 -------- //
  private int code; //코드
  private String message; //메시지

  BaseResponseStatus(int code, String message) {
    this.code = code;
    this.message = message;
  }
}
