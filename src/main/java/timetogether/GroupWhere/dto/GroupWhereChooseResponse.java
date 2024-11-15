package timetogether.GroupWhere.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupWhereChooseResponse {
  private Long groupWhereId;
  private Long groupId;
  private String groupWhereName;
  private String groupWhereUrl;
  private Long groupMeetingId;
  private boolean groupWhereChooseThis =  true;

  @Builder
  public GroupWhereChooseResponse(Long groupWhereId, Long groupId, String groupWhereName, String groupWhereUrl, Long groupMeetingId, boolean groupWhereChooseThis) {
    this.groupWhereId = groupWhereId;
    this.groupId = groupId;
    this.groupWhereName = groupWhereName;
    this.groupWhereUrl = groupWhereUrl;
    this.groupMeetingId = groupMeetingId;
    this.groupWhereChooseThis = groupWhereChooseThis;
  }
}
