package timetogether.group.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timetogether.global.response.BaseResponseStatus;
import timetogether.group.Group;
import timetogether.group.dto.*;
import timetogether.group.exception.*;
import timetogether.group.repository.GroupQueryRepository;
import timetogether.group.repository.GroupRepository;
import timetogether.oauth2.entity.User;
import timetogether.oauth2.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupService {

  private final GroupRepository groupRepository;
  private final GroupQueryRepository groupQueryRepository;
  private final UserRepository userRepository;

  public GroupCreateResponseDto createGroup(String socialId, GroupCreateRequestDto request){
    Group newGroup = new Group(request, socialId);//Group 객체 생성
    newGroup.makeUrl();
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
    //방장이 아닌 그룹 멤버인 경우
    foundGroup.removeUserFromGroup(userRepository.findBySocialId(socialId).get());
    groupRepository.save(foundGroup);
    return new GroupLeaveResponseDto(socialId,"그룹을 나가는데 성공하였습니다.");
  }
//
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
//  public void getIntoGroup(String socialId, Long groupId) throws GroupNotFoundException {
//    Optional<Group> groupFound = groupQueryRepository.findByGroupId(groupId);
//    Group group = groupFound.orElseThrow(() -> new GroupNotFoundException(BaseResponseStatus.NOT_EXIST_GROUPID));
//    group.addGroupSocailId(socialId);
//  }
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
//
//
//
//
//  public List<GroupShowResponseDto> showGroupsWhereSocialIdIn(String socialId) {
//    List<GroupShowResponseDto> response = groupQueryRepository.findGroupsWhereSocialIdIn(socialId);
//    return response;
//  }
}