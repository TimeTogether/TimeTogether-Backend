package timetogether.group;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timetogether.group.dto.GroupAddDatesRequestDto;
import timetogether.group.dto.GroupAddDatesResponseDto;
import timetogether.group.dto.GroupCreateRequestDto;
import timetogether.group.dto.GroupUpdateRequestDto;
import timetogether.groupMeeting.GroupMeeting;
import timetogether.groupMeeting.MeetType;
import timetogether.when2meet.When2meet;
import timetogether.GroupWhere.GroupWhere;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Table(name = "group")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Group {
  @Column(name = "group_id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long groupId;
  @NotNull
  private String groupName; //GroupCreateRequestDto에서 정함
  //private String groupTitle; //GroupCreateRequestDto에서 정함
  private String groupImg; //GroupCreateRequestDto에서 정함
  @NotNull
  private String groupMgrId; //GroupService의 groupCreateDto에서 정함
  private String groupTimes; //GroupAddDatesRequestDto에서 정함  "2024-11-14,2024-11-15"
  //private String date; //GroupAddDatesResponseDto에서 정함
  //private MeetType meetType; //GroupCreateRequestDto에서 정함
  private String groupWhereUrl; //GroupAddDatesResponseDto에서 정함
  //private String groupMembers = null;

  @OneToMany(mappedBy = "group",cascade = CascadeType.REMOVE, orphanRemoval = true)
  private List<GroupMeeting> groupMeetingList;

  @OneToMany(mappedBy = "group", cascade = CascadeType.REMOVE, orphanRemoval = true)
  private List<GroupWhere> groupWhereList = new ArrayList<>();

  @Builder
  public Group(String groupName, String groupTitle, String groupImg, String groupMgrId, String groupTimes, String date, MeetType meetType, String groupWhereUrl,String groupMembers) {
    this.groupName = groupName;
    this.groupTitle = groupTitle;
    this.groupImg = groupImg;
    this.groupMgrId = groupMgrId;
    this.groupTimes = groupTimes;
    this.date = date;
    this.meetType = meetType;
    this.groupWhereUrl = groupWhereUrl;
    this.groupMembers = groupMembers;
  }

  public Group(GroupCreateRequestDto request, String socialId){
    this.groupName = request.getGroupName();
    this.groupMgrId = socialId;
    this.groupImg = request.getGroupImg();
    this.groupTitle = request.getGroupTitle();
    this.meetType = request.getMeetType();
  }

  public Group addGroupTimes(GroupAddDatesRequestDto request){
    this.groupTimes = request.getGroupTimes();
    return this;
  }
  public void addDatesAndUrl(GroupAddDatesResponseDto groupAddDatesResponseDto) {
    this.date = groupAddDatesResponseDto.getDate();
    this.groupWhereUrl = groupAddDatesResponseDto.getGroupWhereUrl();
  }

  public Group update(GroupUpdateRequestDto request) {
    this.groupName = request.getGroupName();
    this.groupTitle = request.getGroupTitle();
    this.groupImg = request.getGroupImg();
    this.groupTimes = request.getGroupTimes();
    this.meetType = request.getMeetType();
    this.groupMembers = request.getGroupMembers();
    return this;
  }

  public void removeFromGroup(String socialId) {
    String[] memberIds = this.getGroupMembers().split(",");
    List<String> updatedMembers = Arrays.stream(memberIds)
            .map(String::trim)
            .filter(id -> !id.equals(socialId))
            .collect(Collectors.toList());
    this.groupMembers = String.join(",", updatedMembers);
  }

  public List<String> parserGroupMembers(){
    String[] split = this.groupMembers.split(",");
    return Arrays.stream(split).map(String::trim).collect(Collectors.toList());
  }


  public void addGroupSocailId(String socialId) {
    if (this.groupMembers == null){
      this.groupMembers = socialId;
    }else{
      this.groupMembers += "," + socialId;
    }
  }

  public void addWhen2meet(When2meet when2meet) {
    this.when2meetList.add(when2meet);
  }
}