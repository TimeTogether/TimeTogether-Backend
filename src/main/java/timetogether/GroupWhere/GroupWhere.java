package timetogether.GroupWhere;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timetogether.group.Group;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupWhere {
  @Column(name = "groupWhere_id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "group_id")
  private Group group;

  private String groupWhereName;
  private String groupWhereUrl;
  private int count = 0;
  private boolean chooseThis = false;

  @Builder
  public GroupWhere(String groupWhereName,String groupWhereUrl,Group group) {
    this.groupWhereName = groupWhereName;
    this.groupWhereUrl = groupWhereUrl;
    this.group = group;
  }

  public void addCount(){
    this.count++;
  }
  public void doneChooseThis(){
    this.chooseThis = true;
  }
}