package timetogether.groupMeeting.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GroupMeetingCreateResponse {
  private Long groupId;
  private String groupMeetingTitle;
  private String GroupMeetingDays;

  @Builder
  public GroupMeetingCreateResponse(Long groupId, String groupMeetingTitle, String groupMeetingDays) {
    this.groupId = groupId;
    this.groupMeetingTitle = groupMeetingTitle;
    GroupMeetingDays = groupMeetingDays;
  }
}
