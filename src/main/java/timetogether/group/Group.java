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
import timetogether.meeting.MeetType;
import timetogether.when2meet.When2meet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
  private String groupName; //GroupCreateRequestDto에서 정함
  private String groupTitle; //GroupCreateRequestDto에서 정함
  private String groupImg; //GroupCreateRequestDto에서 정함
  @NotNull
  private String groupMgrId; //GroupService의 groupCreateDto에서 정함
  private String groupTimes; //GroupAddDatesRequestDto에서 정함
  private String date; //GroupAddDatesResponseDto에서 정함
  private MeetType meetType; //GroupCreateRequestDto에서 정함
  private String groupUrl; //GroupAddDatesResponseDto에서 정함
  private String groupMembers;

  @OneToMany(mappedBy = "group",cascade = CascadeType.REMOVE, orphanRemoval = true)
  private List<When2meet> when2meetList = new ArrayList<>();

  @Builder
  public Group(String groupName, String groupTitle, String groupImg, String groupMgrId, String groupTimes, String date, MeetType meetType, String groupUrl,String groupMembers) {
    this.groupName = groupName;
    this.groupTitle = groupTitle;
    this.groupImg = groupImg;
    this.groupMgrId = groupMgrId;
    this.groupTimes = groupTimes;
    this.date = date;
    this.meetType = meetType;
    this.groupUrl = groupUrl;
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
    this.groupUrl = groupAddDatesResponseDto.getGroupUrl();
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


}