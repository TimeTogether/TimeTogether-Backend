package timetogether.GroupWhere.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupWhereCreateRequestDto {
  private String groupWhereName;
  private String groupWhereUrl;

  @Builder
  public GroupWhereCreateRequestDto(String groupWhereName, String groupWhereUrl) {
    this.groupWhereName = groupWhereName;
    this.groupWhereUrl = groupWhereUrl;
  }
}
