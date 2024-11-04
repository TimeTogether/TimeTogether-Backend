package timetogether.domain.group.dto;

import lombok.Builder;
import lombok.Getter;
import timetogether.domain.MeetType;

@Getter
public class GroupCreateResponseDto {
  private String groupName;
  private String groupTitle;
  private String groupImg;
  private String groupTimes;
  private String date;
  private MeetType meetType;
  private String groupUrl;

  @Builder
  public GroupCreateResponseDto(String groupName, String groupTitle, String groupImg, String groupTimes, String date, MeetType meetType, String groupUrl) {
    this.groupName = groupName;
    this.groupTitle = groupTitle;
    this.groupImg = groupImg;
    this.groupTimes = groupTimes;
    this.date = date;
    this.meetType = meetType;
    this.groupUrl = groupUrl;
  }
}
