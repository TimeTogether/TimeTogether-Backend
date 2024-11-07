package timetogether.group.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timetogether.global.response.BaseResponse;
import timetogether.group.Group;
import timetogether.group.dto.GroupCreateRequestDto;
import timetogether.group.dto.GroupCreateResponseDto;
import timetogether.group.exception.NotValidMembersException;
import timetogether.group.repository.GroupRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupService {

  private final GroupRepository groupRepository;

  public GroupCreateResponseDto createGroup(String socialId, GroupCreateRequestDto request) throws NotValidMembersException {
    Group newGroup = request.transferToGroup(socialId);//groupMgrId 설정
    Group savedGroup =  groupRepository.save(newGroup);
    return GroupCreateResponseDto.from(savedGroup);
  }

  public GroupCreateResponseDto editGroup(String socialId, Long groupId, GroupCreateRequestDto request) {
    return null;
  }
}
