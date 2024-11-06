package timetogether.group.dto;

import lombok.Builder;
import lombok.Getter;
import timetogether.meeting.MeetType;

@Getter
public class GroupCreateRequestDto {
  private String groupName;
  private String groupTitle;
  private String groupImg;
  private String groupMgrId;
  private String groupTimes;
  private String date;
  private MeetType meetType;
  private String groupUrl;


  @Builder
  public GroupCreateRequestDto(String groupName, String groupTitle, String groupImg, String groupMgrId, String groupTimes, String date, MeetType meetType, String groupUrl) {
    this.groupName = groupName;
    this.groupTitle = groupTitle;
    this.groupImg = groupImg;
    this.groupMgrId = groupMgrId;
    this.groupTimes = groupTimes;
    this.date = date;
    this.meetType = meetType;
    this.groupUrl = groupUrl;
  }
}
