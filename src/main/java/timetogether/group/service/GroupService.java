package timetogether.group.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import timetogether.group.dto.GroupCreateRequestDto;
import timetogether.group.dto.GroupCreateResponseDto;
import timetogether.group.repository.GroupRepository;

@Service
@RequiredArgsConstructor
public class GroupService {
  private final GroupRepository groupRepository;
  //요거 수정중
  public GroupCreateResponseDto createGroup(String socialId, GroupCreateRequestDto request) {
    return null;
  }

  public GroupCreateResponseDto editGroup(String socialId, Long groupId, GroupCreateRequestDto request) {
    return null;
  }
}
