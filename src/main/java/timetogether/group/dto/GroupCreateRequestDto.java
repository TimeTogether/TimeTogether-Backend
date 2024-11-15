package timetogether.group.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupCreateRequestDto {
  private String groupName;
  private String groupImg;
  private String groupIntro;

  @Builder
  public GroupCreateRequestDto(String groupName, String groupImg,String groupIntro) {
    this.groupName = groupName;
    this.groupImg = groupImg;
    this.groupIntro = groupIntro;
  }
}