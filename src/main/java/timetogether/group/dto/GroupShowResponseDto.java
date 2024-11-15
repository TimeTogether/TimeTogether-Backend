package timetogether.group.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GroupShowResponseDto {
  private Long groupId;
  private String groupName;
  private String groupTitle;
  private String groupImg;
  private String groupMembers;
  private String groupMgrId;

  @Builder
  public GroupShowResponseDto(Long groupId, String groupName, String groupTitle, String groupImg, String groupMembers,String groupMgrId) {
    this.groupId = groupId;
    this.groupName = groupName;
    this.groupTitle = groupTitle;
    this.groupImg = groupImg;
    this.groupMembers = groupMembers;
    this.groupMgrId = groupMgrId;
  }
}
