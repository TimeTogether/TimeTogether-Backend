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
    Group newGroup = new Group(request, socialId);//groupMgrId 설정후 Group 객체로 변경
    Group savedGroup =  groupRepository.save(newGroup);
    return GroupCreateResponseDto.from(savedGroup);
  }

  public GroupAddDatesResponseDto addDates(String socialId, GroupAddDatesRequestDto request) throws GroupNotFoundOrNotMgrException, GroupTimesLimitSevenDays {
    Group groupFound = groupQueryRepository.findByGroupNameAndIsMgr(socialId, request.getGroupName())
            .orElseThrow(() -> new GroupNotFoundOrNotMgrException(BaseResponseStatus.NOT_VALID_MGR_OR_GROUPNAME));
    groupFound.addGroupTimes(request); //날짜 추가
    GroupAddDatesResponseDto groupAddDatesResponseDto = new GroupAddDatesResponseDto(groupFound);
    groupFound.addDatesAndUrl(groupAddDatesResponseDto);
    groupRepository.save(groupFound);
    return  groupAddDatesResponseDto;
  }

  public boolean checkGroupMembers(String socialId, Long groupId) throws GroupNotFoundException {
    Optional<Group> groupFound = groupQueryRepository.findByGroupId(groupId);
    Group group = groupFound.orElseThrow(() -> new GroupNotFoundException(BaseResponseStatus.NOT_EXIST_GROUPID));
    if (group.getGroupMgrId().equals(socialId)) {
      return true;
    } else {//그룹 매니저가 아니면
      String groupMembers = group.getGroupMembers();
      if (groupMembers == null || groupMembers.isEmpty()) {
        return false;
      }

      String[] memberIds = groupMembers.split(",");
      for (String memberId : memberIds) {
        if (memberId.trim().equals(socialId)) {
          return true;
        }
      }
      return false;
    }
  }

  public String getInvitationCode(Long groupId) throws GroupNotFoundException {
    return groupQueryRepository.findById(groupId)
            .orElseThrow(() ->  new GroupNotFoundException(BaseResponseStatus.NOT_EXIST_GROUPID));
  }

  public void getIntoGroup(String socialId, Long groupId) throws GroupNotFoundException {
    Optional<Group> groupFound = groupQueryRepository.findByGroupId(groupId);
    Group group = groupFound.orElseThrow(() -> new GroupNotFoundException(BaseResponseStatus.NOT_EXIST_GROUPID));
    group.addGroupSocailId(socialId);
  }

  private boolean validateGroupMembers(String groupMembers) {
    String[] memberIds = groupMembers.split(",");
    return Arrays.stream(memberIds)
            .map(String::trim)
            .allMatch(userRepository::existsById);
  }

//  public GroupUpdateResponseDto editGroup(String socialId, Long groupId, GroupUpdateRequestDto request) throws GroupNotFoundException, NotValidMemberException, NotGroupMgrInGroup {
//    Group foundGroup = groupRepository.findById(groupId)
//            .orElseThrow(()->new GroupNotFoundException(BaseResponseStatus.NOT_EXIST_GROUPID));
//    boolean isMgr = foundGroup.getGroupMgrId().equals(socialId); //방장인 경우 판별
//    if (!isMgr){
//      throw new NotGroupMgrInGroup(BaseResponseStatus.NOT_VALID_MGR);
//    }
//
//    Group updatedGroup = foundGroup.update(request);
//
//    boolean ok = validateGroupMembers(request.getGroupMembers());
//    if (!ok){
//      throw new NotValidMemberException(BaseResponseStatus.NOT_VALID_USER);
//    }
//    Group savedGroup = groupRepository.save(updatedGroup);
//    return GroupUpdateResponseDto.from(savedGroup);
//
//  }

  public String deleteGroup(String socialId, Long groupId) throws GroupNotFoundException, NotGroupMgrInGroup {
    Group foundGroup = groupRepository.findById(groupId)
            .orElseThrow(()->new GroupNotFoundException(BaseResponseStatus.NOT_EXIST_GROUPID));
    boolean isMgr = foundGroup.getGroupMgrId().equals(socialId); //방장인 경우 판별
    if (!isMgr){
      throw new NotGroupMgrInGroup(BaseResponseStatus.NOT_VALID_MGR);
    }
    groupRepository.delete(foundGroup);//그룹 지우기 (일단 회의일정 확정전)
    return "그룹을 삭제하였습니다.";
  }

  public GroupLeaveResponseDto leaveGroup(String socialId, Long groupId) throws GroupNotFoundException, NotValidMemberException, NotAllowedGroupMgrToLeave {
    Group foundGroup = groupRepository.findById(groupId)
            .orElseThrow(()->new GroupNotFoundException(BaseResponseStatus.NOT_EXIST_GROUPID));
    boolean isExistInGroup = validateSocialId(foundGroup,socialId);
    if (!isExistInGroup){
      throw new NotValidMemberException(BaseResponseStatus.NOT_VALID_USER);
    }
    boolean isMgr = foundGroup.getGroupMgrId().equals(socialId); //방장인 경우 판별
    if (isMgr){
      throw new NotAllowedGroupMgrToLeave(BaseResponseStatus.NOT_VALID_MGR);
    }
    foundGroup.removeFromGroup(socialId);
    groupRepository.save(foundGroup);
    return new GroupLeaveResponseDto(socialId,"그룹을 나가는데 성공하였습니다.");
  }

  private boolean validateSocialId(Group foundGroup, String socialId) throws NotValidMemberException {
    String[] memberIds = foundGroup.getGroupMembers().split(",");
    for (String id : memberIds){
      if (id != null){
        if (id.equals(socialId))
          return true;
      }
    }
    return false;
  }

  public List<GroupShowResponseDto> showGroupsWhereSocialIdIn(String socialId) {
    List<GroupShowResponseDto> response = groupQueryRepository.findGroupsWhereSocialIdIn(socialId);
    return response;
  }
}