package timetogether.group.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timetogether.group.Group;
import timetogether.group.dto.GroupCreateRequestDto;
import timetogether.group.dto.GroupCreateResponseDto;
import timetogether.group.repository.GroupRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupService {

  private final GroupRepository groupRepository;
  //요거 수정중
  public GroupCreateResponseDto createGroup(String socialId, GroupCreateRequestDto request) {
//    Group newGroup = update(request);
//    groupRepository.create(socialId, request);
    return null;
  }

  public GroupCreateResponseDto editGroup(String socialId, Long groupId, GroupCreateRequestDto request) {
    return null;
  }
}
