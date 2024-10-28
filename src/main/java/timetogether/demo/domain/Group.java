package timetogether.demo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Group {
  @Column(name = "group_id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String groupName;
  private String groupImg;
  private int groupMgrId;
  private String groupTimes;
  private String groupUrl;

  @Builder
  public Group(String groupName, String groupImg, int groupMgrId, String groupTimes, String groupUrl) {
    this.groupName = groupName;
    this.groupImg = groupImg;
    this.groupMgrId = groupMgrId;
    this.groupTimes = groupTimes;
    this.groupUrl = groupUrl;
  }
}
