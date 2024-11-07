package timetogether.group.dto;

import lombok.Getter;

@Getter
public class GroupLeaveResponseDto {
  private String socialId;
  private String message;

  public GroupLeaveResponseDto(String socialId, String message) {
    this.socialId = socialId;
    this.message = message;
  }
}