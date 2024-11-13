package timetogether.group.dto;

import lombok.Getter;

@Getter
public class GroupDeleteResponseDto {
  private Long groupId;
  private String message;

  public GroupDeleteResponseDto(Long groupId, String message) {
    this.groupId = groupId;
    this.message = message;
  }
}
