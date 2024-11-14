package timetogether.group.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timetogether.groupMeeting.MeetType;

@Getter
@NoArgsConstructor
public class GroupCreateRequestDto {
  private String groupName;
  private String groupTitle;
  private String groupImg;
  private MeetType meetType;


  @Builder
  public GroupCreateRequestDto(String groupName, String groupTitle, String groupImg,MeetType meetType) {
    this.groupName = groupName;
    this.groupTitle = groupTitle;
    this.groupImg = groupImg;
    this.meetType = meetType;
  }
}