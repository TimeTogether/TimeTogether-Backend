package timetogether.where2meet.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupWhereChooseResponse {
  private String groupLocationName;
  private String groupWhereUrl;
  private boolean groupWhereChooseThis =  true;

  @Builder
  public GroupWhereChooseResponse(String groupLocationName, String groupWhereUrl) {
    this.groupLocationName = groupLocationName;
    this.groupWhereUrl = groupWhereUrl;
  }
}
