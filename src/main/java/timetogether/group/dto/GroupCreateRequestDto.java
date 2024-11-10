package timetogether.group.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timetogether.group.Group;
import timetogether.group.exception.NotValidMemberException;
import timetogether.meeting.MeetType;

import java.util.Arrays;
import java.util.Optional;

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