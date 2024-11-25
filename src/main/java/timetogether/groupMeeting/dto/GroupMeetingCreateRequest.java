package timetogether.groupMeeting.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GroupMeetingCreateRequest {
  private Long groupId;
  private String groupMeetingTitle;
  private String GroupMeetingDays;

  @Builder
  public GroupMeetingCreateRequest(Long groupId, String groupMeetingTitle, String groupMeetingDays) {
    this.groupId = groupId;
    this.groupMeetingTitle = groupMeetingTitle;
    GroupMeetingDays = groupMeetingDays;
  }
}
