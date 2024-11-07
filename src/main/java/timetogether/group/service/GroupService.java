package timetogether.group.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timetogether.global.response.BaseResponseStatus;
import timetogether.group.Group;
import timetogether.group.dto.*;
import timetogether.group.exception.GroupNotFoundException;
import timetogether.group.exception.NotAllowedGroupMgrToLeave;
import timetogether.group.exception.NotGroupMgrInGroup;
import timetogether.group.exception.NotValidMemberException;
import timetogether.group.repository.GroupRepository;
import timetogether.oauth2.repository.UserRepository;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupService {

  private final GroupRepository groupRepository;
  private final UserRepository userRepository;

  public GroupCreateResponseDto createGroup(String socialId, GroupCreateRequestDto request) throws NotValidMemberException {
    Group newGroup = request.transferToGroup(socialId);//groupMgrId 설정
    //groupmembers 유효 검사
    boolean ok = validateGroupMembers(newGroup.getGroupMembers());
    if (!ok){
      throw new NotValidMemberException(BaseResponseStatus.NOT_VALID_USER);
    }
    Group savedGroup =  groupRepository.save(newGroup);
    return GroupCreateResponseDto.from(savedGroup);
  }

  private boolean validateGroupMembers(String groupMembers) {
    String[] memberIds = groupMembers.split(",");
    return Arrays.stream(memberIds)
            .map(String::trim)
            .allMatch(userRepository::existsById);
  }

  public GroupUpdateResponseDto editGroup(String socialId, Long groupId, GroupUpdateRequestDto request) throws GroupNotFoundException, NotValidMemberException, NotGroupMgrInGroup {

    Group foundGroup = groupRepository.findById(groupId)
            .orElseThrow(()->new GroupNotFoundException(BaseResponseStatus.NOT_EXIST_GROUPID));
    boolean isMgr = foundGroup.getGroupMgrId().equals(socialId); //방장인 경우 판별
    if (!isMgr){
      throw new NotGroupMgrInGroup(BaseResponseStatus.NOT_VALID_MGR);
    }

    Group updatedGroup = foundGroup.update(request);

    boolean ok = validateGroupMembers(request.getGroupMembers());
    if (!ok){
      throw new NotValidMemberException(BaseResponseStatus.NOT_VALID_USER);
    }
    Group savedGroup = groupRepository.save(updatedGroup);
    return GroupUpdateResponseDto.from(savedGroup);

  }

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
    return Arrays.stream(memberIds)
            .map(String::trim)
            .equals(socialId);
  }
}
