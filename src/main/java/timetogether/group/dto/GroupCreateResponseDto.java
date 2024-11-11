package timetogether.group.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timetogether.group.Group;
import timetogether.meeting.MeetType;

@Getter
@NoArgsConstructor
public class GroupCreateResponseDto {
  private String groupName;
  private String groupTitle;
  private String groupImg;
  private String groupMgrId;
  private MeetType meetType;

  @Builder
  public GroupCreateResponseDto(String groupName, String groupTitle, String groupImg, String groupMgrId, MeetType meetType) {
    this.groupName = groupName;
    this.groupTitle = groupTitle;
    this.groupImg = groupImg;
    this.groupMgrId = groupMgrId;
    this.meetType = meetType;
  }


  public static GroupCreateResponseDto from(Group savedGroup) {
    return GroupCreateResponseDto.builder()
            .groupName(savedGroup.getGroupName())
            .groupTitle(savedGroup.getGroupTitle())
            .groupImg(savedGroup.getGroupImg())
            .groupMgrId(savedGroup.getGroupMgrId())
            .meetType(savedGroup.getMeetType())
            .build();
  }
}