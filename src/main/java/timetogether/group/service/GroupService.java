package timetogether.group.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timetogether.global.response.BaseResponseStatus;
import timetogether.group.Group;
import timetogether.group.dto.*;
import timetogether.group.exception.*;
import timetogether.group.repository.GroupQueryRepository;
import timetogether.group.repository.GroupRepository;
import timetogether.oauth2.entity.User;
import timetogether.oauth2.repository.UserQueryRepository;
import timetogether.oauth2.repository.UserRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class GroupService {

  private final GroupRepository groupRepository;
  private final GroupQueryRepository groupQueryRepository;
  private final UserRepository userRepository;
  private final UserQueryRepository userQueryRepository;

  public GroupCreateResponseDto createGroup(String socialId, GroupCreateRequestDto request){
    Group newGroup = new Group(request, socialId);//Group 객체 생성
    newGroup.makeUrl();
    Optional<User> user = userRepository.findBySocialId(socialId);
    newGroup.addGroupSocailId(user.get());
    Group savedGroup =  groupRepository.save(newGroup);
    return GroupCreateResponseDto.from(savedGroup);
  }

  public String deleteGroup(String socialId, Long groupId) throws GroupNotFoundException, NotGroupMgrInGroup {
    Group foundGroup = groupRepository.findById(groupId)
            .orElseThrow(()->new GroupNotFoundException(BaseResponseStatus.NOT_EXIST_GROUPID));
    boolean isMgr = foundGroup.getGroupMgrId().equals(socialId); //방장인 경우 판별
    if (!isMgr){
      throw new NotGroupMgrInGroup(BaseResponseStatus.NOT_VALID_MGR);
    }
    userQueryRepository.deleteGroupInUserEntity(foundGroup.getGroupUserList(),foundGroup);
    groupRepository.delete(foundGroup);//그룹 지우기
    return "그룹을 삭제하였습니다.";
  }

  public boolean checkGroupMembers(String socialId, Long groupId) throws GroupNotFoundException {
    Optional<Group> groupFound = groupQueryRepository.findByGroupId(groupId);
    Group group = groupFound.orElseThrow(() -> new GroupNotFoundException(BaseResponseStatus.NOT_EXIST_GROUPID));

    return validateGroupMembers(socialId, group);
  }

  private boolean validateGroupMembers(String socialId, Group group) {
    // 1. 먼저 그룹 매니저 체크
    if (group.getGroupMgrId().equals(socialId)) {
      return true;
    }

    // 2. 그룹 멤버 리스트 체크
    List<User> groupMembersExceptMgr = group.getGroupUserList();
    if (groupMembersExceptMgr == null || groupMembersExceptMgr.isEmpty()) {
      return false;
    }

    // 3. Stream을 사용해서 socialId가 존재하는지 확인
    return groupMembersExceptMgr.stream()
            .map(User::getSocialId)
            .anyMatch(memberId -> memberId.trim().equals(socialId));
  }

  public String getInvitationCode(Long groupId) throws GroupNotFoundException {
    return groupQueryRepository.findById(groupId)
            .orElseThrow(() ->  new GroupNotFoundException(BaseResponseStatus.NOT_EXIST_GROUPID));
  }

  public GroupLeaveResponseDto leaveGroup(String socialId, Long groupId) throws GroupNotFoundException, NotValidMemberException, NotAllowedGroupMgrToLeave {
    Group foundGroup = groupRepository.findById(groupId)
            .orElseThrow(()->new GroupNotFoundException(BaseResponseStatus.NOT_EXIST_GROUPID));
    boolean isExistInGroup = validateGroupMembers(socialId,foundGroup);
    if (!isExistInGroup){
      throw new NotValidMemberException(BaseResponseStatus.NOT_VALID_USER);
    }
    boolean isMgr = foundGroup.getGroupMgrId().equals(socialId); //방장인 경우 판별
    if (isMgr){
      throw new NotAllowedGroupMgrToLeave(BaseResponseStatus.NOT_VALID_MGR);
    }
    log.info("groupUserList {} ", foundGroup.getGroupUserList()); //힝..
    //방장이 아닌 그룹 멤버인 경우
    foundGroup.removeUserFromGroup(userRepository.findBySocialId(socialId).get());
    groupRepository.save(foundGroup);
    return new GroupLeaveResponseDto(socialId,"그룹을 나가는데 성공하였습니다.");
  }

  public Long getGroupIdFromCode(String groupUrl) throws GroupNotFoundException {
    return groupQueryRepository.findGroupIdByGroupUrl(groupUrl)
            .orElseThrow(() ->  new GroupNotFoundException(BaseResponseStatus.NOT_EXIST_GROUPID));
  }

  public void getIntoGroup(String socialId, Long groupId) throws GroupNotFoundException, NotValidMemberException {
    User addingUser = userRepository.findBySocialId(socialId)
            .orElseThrow(() -> new NotValidMemberException(BaseResponseStatus.NOT_VALID_USER));
    Group groupFound = groupQueryRepository.findByGroupId(groupId)
            .orElseThrow(() -> new GroupNotFoundException(BaseResponseStatus.NOT_EXIST_GROUPID));

    groupFound.addGroupSocailId(addingUser);
    groupRepository.save(groupFound);
  }

  public List<GroupShowResponseDto> showGroupsWhereSocialIdIn(String socialId) {

    // UserRepository를 통해 그룹 조회
    List<Group> groups = userQueryRepository.findGroupsBySocialId(socialId);

    // GroupRepository를 통해 groupMgrId와 socialId가 같은 그룹 조회
    List<Group> managedGroups = groupQueryRepository.findGroupsByManagerId(socialId);

    // 두 결과를 합치되, 중복 제거
    List<Group> combinedGroups = Stream.concat(groups.stream(), managedGroups.stream())
            .distinct() // 중복 제거
            .collect(Collectors.toList());
    // Group 정보를 DTO로 변환
    return combinedGroups.stream()
            .map(group -> GroupShowResponseDto.builder()
                    .groupId(group.getId())
                    .groupName(group.getGroupName())
                    .groupImg(group.getGroupImg())
                    .groupMgrId(group.getGroupMgrId())
                    .userNameResponseList(group.getGroupUserList().stream()
                            .map(user -> new UserNameResponse(user.getUserName()))
                            .collect(Collectors.toList()))
                    .groupTimes(group.getGroupTimes())
                    .groupIntro(group.getGroupIntro())
                    .groupUrl(group.getGroupUrl())
                    .build())
            .collect(Collectors.toList());
  }
}

//  public GroupAddDatesResponseDto addDates(String socialId, GroupAddDatesRequestDto request) throws GroupNotFoundOrNotMgrException, GroupTimesLimitSevenDays {
//    Group groupFound = groupQueryRepository.findByGroupNameAndIsMgr(socialId, request.getGroupName())
//            .orElseThrow(() -> new GroupNotFoundOrNotMgrException(BaseResponseStatus.NOT_VALID_MGR_OR_GROUPNAME));
//    groupFound.addGroupTimes(request); //날짜 추가
//    GroupAddDatesResponseDto groupAddDatesResponseDto = new GroupAddDatesResponseDto(groupFound);
//    groupFound.addDatesAndUrl(groupAddDatesResponseDto);
//    groupRepository.save(groupFound);
//    return  groupAddDatesResponseDto;
//  }
//
//
//
//
//  private boolean validateGroupMembers(String groupMembers) {
//    String[] memberIds = groupMembers.split(",");
//    return Arrays.stream(memberIds)
//            .map(String::trim)
//            .allMatch(userRepository::existsById);
//  }
//
////  public GroupUpdateResponseDto editGroup(String socialId, Long groupId, GroupUpdateRequestDto request) throws GroupNotFoundException, NotValidMemberException, NotGroupMgrInGroup {
////    Group foundGroup = groupRepository.findById(groupId)
////            .orElseThrow(()->new GroupNotFoundException(BaseResponseStatus.NOT_EXIST_GROUPID));
////    boolean isMgr = foundGroup.getGroupMgrId().equals(socialId); //방장인 경우 판별
////    if (!isMgr){
////      throw new NotGroupMgrInGroup(BaseResponseStatus.NOT_VALID_MGR);
////    }
////
////    Group updatedGroup = foundGroup.update(request);
////
////    boolean ok = validateGroupMembers(request.getGroupMembers());
////    if (!ok){
////      throw new NotValidMemberException(BaseResponseStatus.NOT_VALID_USER);
////    }
////    Group savedGroup = groupRepository.save(updatedGroup);
////    return GroupUpdateResponseDto.from(savedGroup);
////
////  }
