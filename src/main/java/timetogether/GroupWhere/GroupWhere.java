package timetogether.GroupWhere;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timetogether.group.Group;
import timetogether.groupMeeting.GroupMeeting;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "group_where")
public class GroupWhere {
  @Column(name = "groupWhere_id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "group_id")
  private Group group;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "groupMeet_id")
  private GroupMeeting groupMeeting;

  private String groupWhereName;
  private String groupWhereUrl;
  private int count = 0;
  private boolean chooseThis = false;

  @Builder
  public GroupWhere(String groupWhereName,String groupWhereUrl,Group group,GroupMeeting groupMeeting) {
    this.groupWhereName = groupWhereName;
    this.groupWhereUrl = groupWhereUrl;
    this.group = group;
    this.groupMeeting = groupMeeting;
  }

  @Builder
  public GroupWhere(String groupWhereName,String groupWhereUrl) {
    this.groupWhereName = groupWhereName;
    this.groupWhereUrl = groupWhereUrl;
  }

  public void changeCount(Long upAndDown){
    if (upAndDown == 1 ){
      this.count++;
    }else if (upAndDown == 0){
      if (this.count == 0){
        return;
      }else if (this.count > 0){
        this.count--;
      }
    }
  }
  public void doneChooseThis(){
    this.chooseThis = true;
  }

  public void settingGroup(Group group) {
    this.group = group;
  }

  public void settingGroupMeeting(GroupMeeting groupMeeting) {
    this.groupMeeting = groupMeeting;
  }
}