package timetogether.domain.group.dto;

import lombok.Builder;
import lombok.Getter;
import timetogether.domain.MeetType;

@Getter
public class GroupCreateRequestDto {
  private String groupName;
  private String groupTitle;
  private String groupImg;
  private String groupTimes;
  private String date;
  private MeetType meetType;

  @Builder
  public GroupCreateRequestDto(String groupName, String groupTitle, String groupImg, String groupTimes, String date, MeetType meetType) {
    this.groupName = groupName;
    this.groupTitle = groupTitle;
    this.groupImg = groupImg;
    this.groupTimes = groupTimes;
    this.date = date;
    this.meetType = meetType;
  }
}
