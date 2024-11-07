package timetogether.group.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timetogether.global.response.BaseResponseStatus;
import timetogether.group.Group;
import timetogether.group.dto.GroupCreateRequestDto;
import timetogether.group.dto.GroupCreateResponseDto;
import timetogether.group.dto.GroupUpdateRequestDto;
import timetogether.group.dto.GroupUpdateResponseDto;
import timetogether.group.exception.GroupNotFoundException;
import timetogether.group.exception.NotGroupMgrInGroup;
import timetogether.group.exception.NotValidMembersException;
import timetogether.group.repository.GroupRepository;
import timetogether.oauth2.repository.UserRepository;

import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupService {

  private final GroupRepository groupRepository;
  private final UserRepository userRepository;

  public GroupCreateResponseDto createGroup(String socialId, GroupCreateRequestDto request) throws NotValidMembersException {
    Group newGroup = request.transferToGroup(socialId);//groupMgrId 설정
    //groupmembers 유효 검사
    boolean ok = validateGroupMembers(newGroup.getGroupMembers());
    if (!ok){
      throw new NotValidMembersException(BaseResponseStatus.NOT_VALID_USER);
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

  public GroupUpdateResponseDto editGroup(String socialId, Long groupId, GroupUpdateRequestDto request) throws GroupNotFoundException, NotValidMembersException, NotGroupMgrInGroup {

    Group foundGroup = groupRepository.findById(groupId)
            .orElseThrow(()->new GroupNotFoundException(BaseResponseStatus.NOT_EXIST_GROUPID));
    boolean isMgr = foundGroup.getGroupMgrId().equals(socialId); //방장인 경우 판별
    if (!isMgr){
      throw new NotGroupMgrInGroup(BaseResponseStatus.NOT_VALID_MGR);
    }

    Group updatedGroup = foundGroup.update(request);

    boolean ok = validateGroupMembers(request.getGroupMembers());
    if (!ok){
      throw new NotValidMembersException(BaseResponseStatus.NOT_VALID_USER);
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
}
