package timetogether.group.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupCreateRequestDto {
  private String groupName;
  private String groupImg;

  @Builder
  public GroupCreateRequestDto(String groupName, String groupImg) {
    this.groupName = groupName;
    this.groupImg = groupImg;
  }
}