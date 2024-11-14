package timetogether.group.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timetogether.group.Group;
import timetogether.groupMeeting.MeetType;

@Getter
@NoArgsConstructor
public class GroupCreateResponseDto {
  private Long groupId;
  private String groupName;
  private String groupTitle;
  private String groupImg;
  private String groupMgrId;
  private MeetType meetType;
  private String groupMembers;

  @Builder
  public GroupCreateResponseDto(Long groupId, String groupName, String groupTitle, String groupImg, String groupMgrId, MeetType meetType,String groupMembers) {
    this.groupId = groupId;
    this.groupName = groupName;
    this.groupTitle = groupTitle;
    this.groupImg = groupImg;
    this.groupMgrId = groupMgrId;
    this.meetType = meetType;
    this.groupMembers = groupMembers;
  }


  public static GroupCreateResponseDto from(Group savedGroup) {
    return GroupCreateResponseDto.builder()
            .groupId(savedGroup.getId())
            .groupName(savedGroup.getGroupName())
            .groupTitle(savedGroup.getGroupTitle())
            .groupImg(savedGroup.getGroupImg())
            .groupMgrId(savedGroup.getGroupMgrId())
            .meetType(savedGroup.getMeetType())
            .groupMembers(savedGroup.getGroupMembers())
            .build();
  }
}