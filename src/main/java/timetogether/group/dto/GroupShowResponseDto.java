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
  private String groupTimes;
  private String groupIntro;
  private String groupUrl;
  private List<UserNameResponse> userNameResponseList;

  @Builder
  public GroupShowResponseDto(Long groupId, String groupName, String groupImg, String groupMgrId, String groupTimes, String groupIntro, String groupUrl, List<UserNameResponse> userNameResponseList) {
    this.groupId = groupId;
    this.groupName = groupName;
    this.groupImg = groupImg;
    this.groupMgrId = groupMgrId;
    this.groupTimes = groupTimes;
    this.groupIntro = groupIntro;
    this.groupUrl = groupUrl;
    this.userNameResponseList = userNameResponseList;
  }
}
