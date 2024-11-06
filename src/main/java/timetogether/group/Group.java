package timetogether.group;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timetogether.group.dto.GroupCreateRequestDto;
import timetogether.meeting.MeetType;
import timetogether.when2meet.When2meet;

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
  private String groupTitle;
  private String groupImg;
  @NotNull
  private String groupMgrId;
  @NotNull
  private String groupTimes;
  private String date;
  private MeetType meetType;
  @NotNull
  private String groupUrl;
  private String groupMembers;

  @OneToMany(mappedBy = "group")
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

  public Group update(GroupCreateRequestDto request) {
    this.groupName = request.getGroupName();
    this.groupTitle = request.getGroupTitle();
    this.groupImg = request.getGroupImg();
    this.groupTimes = request.getGroupTimes();
    this.meetType = request.getMeetType();
    this.groupUrl = request.getGroupUrl();
    this.groupMembers = request.getGroupMembers();
    return this;
  }
}