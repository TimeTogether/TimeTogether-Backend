package timetogether.where2meet.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupWhereCreateResponseDto {
  private String groupLocationName;
  private String groupWhereUrl;

  @Builder
  public GroupWhereCreateResponseDto(String groupLocationName, String groupWhereUrl) {
    this.groupLocationName = groupLocationName;
    this.groupWhereUrl = groupWhereUrl;
  }
}
