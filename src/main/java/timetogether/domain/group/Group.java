package timetogether.domain.group;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timetogether.domain.When2meet;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "group_table")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Group {
  @Column(name = "group_id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NotNull
  private String groupName;
  private String groupImg;

  @NotNull
  private Long groupMgrId;
  @NotNull
  private String groupTimes;
  @NotNull
  private String groupUrl;

  @OneToMany(mappedBy = "group")
  private List<When2meet> when2meetList = new ArrayList<>();


  @Builder
  public Group(String groupName, String groupImg, Long groupMgrId, String groupTimes, String groupUrl) {
    this.groupName = groupName;
    this.groupImg = groupImg;
    this.groupMgrId = groupMgrId;
    this.groupTimes = groupTimes;
    this.groupUrl = groupUrl;
  }
}