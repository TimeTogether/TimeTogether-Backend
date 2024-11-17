package timetogether.GroupWhere.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GroupWhereVoteResponse {
  private Long groupWhereId;
  private Long groupId;
  private String groupLocationName;
  private String groupWhereUrl;
  private int count;
  private Long groupMeetingId;

  @Builder
  public GroupWhereVoteResponse(Long groupWhereId, Long groupId, String groupLocationName, String groupWhereUrl, int count, Long groupMeetingId) {
    this.groupWhereId = groupWhereId;
    this.groupId = groupId;
    this.groupLocationName = groupLocationName;
    this.groupWhereUrl = groupWhereUrl;
    this.count = count;
    this.groupMeetingId = groupMeetingId;
  }
}
