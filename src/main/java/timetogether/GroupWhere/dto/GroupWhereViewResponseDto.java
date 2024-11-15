package timetogether.GroupWhere.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupWhereViewResponseDto {
  private Long groupWhereId;
  private Long groupId;
  private String groupLocationName;
  private String groupWhereUrl;
  private int count = 0;
  private Long groupMeetingId;

  @Builder
  public GroupWhereViewResponseDto(Long groupWhereId, Long groupId, String groupLocationName, String groupWhereUrl, int count, Long groupMeetingId) {
    this.groupWhereId = groupWhereId;
    this.groupId = groupId;
    this.groupLocationName = groupLocationName;
    this.groupWhereUrl = groupWhereUrl;
    this.count = count;
    this.groupMeetingId = groupMeetingId;
  }
}
