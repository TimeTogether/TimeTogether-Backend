package timetogether.where2meet.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupWhereViewResponseDto {
  private String groupLocationName;
  private String groupWhereUrl;
  private int count = 0;

  @Builder
  public GroupWhereViewResponseDto(String groupLocationName, String groupWhereUrl,int count) {
    this.groupLocationName = groupLocationName;
    this.groupWhereUrl = groupWhereUrl;
    this.count = count;
  }
}
