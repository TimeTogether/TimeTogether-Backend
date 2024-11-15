package timetogether.group.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class GroupShowResponseDto {
  private Long groupId;
  private String groupName;
  private String groupImg;
  private String groupMgrId;
  private List<UserNameResponse> userNameResponseList;

  public GroupShowResponseDto(Long groupId, String groupName, String groupImg, String groupMgrId, List<UserNameResponse> userNameResponseList) {
    this.groupId = groupId;
    this.groupName = groupName;
    this.groupImg = groupImg;
    this.groupMgrId = groupMgrId;
    this.userNameResponseList = userNameResponseList;
  }
}
