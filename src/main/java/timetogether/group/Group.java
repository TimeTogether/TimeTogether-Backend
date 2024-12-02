package timetogether.group;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import timetogether.GroupWhere.GroupWhere;
import timetogether.group.dto.GroupAddDatesRequestDto;
import timetogether.group.dto.GroupCreateRequestDto;
import timetogether.group.dto.GroupUpdateRequestDto;
import timetogether.oauth2.entity.User;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Entity
@Getter
@Table(name = "group_table")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class Group {
  @Column(name = "group_id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NotNull
  private String groupName;
  private String groupImg;
  @NotNull
  private String groupMgrId;
  private String groupTimes = "07002330";
  private String groupIntro;
  private String groupUrl;

  @ManyToMany(mappedBy = "groupList")  // User 엔티티의 필드명을 참조
  private List<User> groupUserList = new ArrayList<>();

  @OneToMany(mappedBy = "group")
  private List<GroupWhere> groupWhereList = new ArrayList<>();

  @Builder
  public Group(String groupName, String groupImg, String groupMgrId,String groupIntro) {
    this.groupName = groupName;
    this.groupImg = groupImg;
    this.groupMgrId = groupMgrId;
    this.groupIntro = groupIntro;
  }

  public Group(GroupCreateRequestDto request, String socialId) {
    this.groupName = request.getGroupName();
    this.groupImg = request.getGroupImg();
    this.groupMgrId = socialId;
    this.groupIntro = request.getGroupIntro();
  }

  public Group addGroupTimes(GroupAddDatesRequestDto request) {
    this.groupTimes = request.getGroupTimes();
    return this;
  }

//  public void addDatesAndUrl(GroupAddDatesResponseDto groupAddDatesResponseDto) {
//    //this.date = groupAddDatesResponseDto.getDate();
//    this.groupWhereUrl = groupAddDatesResponseDto.getGroupWhereUrl();
//  }

  public Group update(GroupUpdateRequestDto request) {
    this.groupName = request.getGroupName();
    this.groupImg = request.getGroupImg();
    this.groupTimes = request.getGroupTimes();
    return this;
  }

  public void makeUrl() {
    StringBuilder urlBuilder = new StringBuilder();
    SecureRandom random = new SecureRandom();
    String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    int URL_LENGTH = 8;

    for (int i = 0; i < URL_LENGTH; i++) {
      int randomIndex = random.nextInt(CHARACTERS.length());
      urlBuilder.append(CHARACTERS.charAt(randomIndex));
    }

    this.groupUrl = urlBuilder.toString();
  }

  public void removeUserFromGroup(User user) {
    int index = groupUserList.indexOf(user);
    groupUserList.remove(index);
    user.removeGroupFromUser(this);
    log.info("userList : {}", groupUserList);
  }

  public void addGroupSocailId(User addingUser) {
    if (addingUser != null) {
      this.groupUserList.add(addingUser);
      addingUser.addSocailIdGroup(this);
      log.info("userList : {}", groupUserList);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Group group = (Group) o;
    return Objects.equals(getId(), group.getId()) && Objects.equals(getGroupName(), group.getGroupName()) && Objects.equals(getGroupMgrId(), group.getGroupMgrId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getGroupName(), getGroupMgrId());
  }

  //
//  public void removeFromGroup(String socialId) {
//    String[] memberIds = this.getGroupMembers().split(",");
//    List<String> updatedMembers = Arrays.stream(memberIds)
//            .map(String::trim)
//            .filter(id -> !id.equals(socialId))
//            .collect(Collectors.toList());
//    this.groupMembers = String.join(",", updatedMembers);
//  }
//
//  public List<String> parserGroupMembers(){
//    String[] split = this.groupMembers.split(",");
//    return Arrays.stream(split).map(String::trim).collect(Collectors.toList());
//  }
//
//
}