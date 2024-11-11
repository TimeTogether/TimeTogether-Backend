package timetogether.global.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BaseResponseStatus {
  // -------- 성공 코드 시작 -------- //
  SUCCESS(HttpStatus.OK, "요청에 성공했습니다."),
  // -------- 성공 코드 종료 -------- //

  // -------- 실패 코드 시작 -------- //
  /**
   * 서버 내부 오류
   */
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),
  /**
   * Calendar
   */
  NOT_FOUND_MEETINGS_IN_CALENDAR(HttpStatus.NOT_FOUND, "등록된 일정이 없습니다."),
  NOT_VALID_CALENDAR_STARTTIME_AFTER_ENDTIME(HttpStatus.BAD_REQUEST, "일정 종료 시각이 일정 시각 시각보다 빠릅니다."),
  NOT_VALID_CALENDAR_TIME_NULL(HttpStatus.BAD_REQUEST,"일정 종료 시각이나 시작 시각 값이 비어있습니다." ),
  NOT_EXIST_MEETINGID(HttpStatus.NOT_FOUND, "존재하지 않은 캘린더 회의일정입니다."),
  /**
   * Group
   */
  NOT_VALID_MEMBERS(HttpStatus.NOT_FOUND, "존재하지 않는 멤버를 입력했습니다."),
  NOT_EXIST_GROUPID(HttpStatus.NOT_FOUND,"존재하지 않는 그룹입니다."),
  NOT_VALID_MGR_OR_GROUPNAME(HttpStatus.NOT_FOUND,"존재하지 않는 그룹명이거나 그룹 매니저가 아닙니다."),
  NOT_VALID_GROUPTIMES(HttpStatus.BAD_REQUEST, "정할 수 있는 기간은 오늘로부터 최대 7일 이내 입니다."),
  NOT_VALID_MGR(HttpStatus.NOT_ACCEPTABLE, "그룹을 수정할 권한이 없습니다."),
  NOT_ALLOWED_TO_LEAVE_FOR_GROUPMGR(HttpStatus.NOT_ACCEPTABLE,"방장은 그룹을 나갈 수 없습니다."),
  ALREADY_EXIST_IN_GROUP(HttpStatus.ALREADY_REPORTED,"이미 그룹에 속해있습니다."),
  /**
   * User
   */
  NOT_VALID_USER(HttpStatus.NOT_FOUND, "유효한 유저가 아닙니다.");
  // -------- 실패 코드 종료 -------- //
  private HttpStatus httpStatus; //상태
  private String message; //메시지

  BaseResponseStatus(HttpStatus httpStatus, String message) {
    this.httpStatus = httpStatus;
    this.message = message;
  }
  BaseResponseStatus(HttpStatus httpStatus) {
    this.httpStatus = httpStatus;
  }
}
