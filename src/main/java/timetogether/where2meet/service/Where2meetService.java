package timetogether.where2meet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timetogether.global.response.BaseResponseStatus;
import timetogether.group.Group;
import timetogether.group.exception.GroupNotFoundException;
import timetogether.group.exception.NotValidMemberException;
import timetogether.group.repository.GroupRepository;
import timetogether.group.service.GroupService;
import timetogether.where2meet.GroupWhere;
import timetogether.where2meet.dto.GroupWhereCreateRequestDto;
import timetogether.where2meet.dto.GroupWhereViewResponseDto;
import timetogether.where2meet.repository.GroupWhereQueryRepository;
import timetogether.where2meet.repository.GroupWhereRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class Where2meetService {
  private GroupRepository groupRepository;
  private GroupService groupService;
  private GroupWhereQueryRepository groupWhereQueryRepository;
  private GroupWhereRepository groupWhereRepository;


  public Optional<List<GroupWhereViewResponseDto>> getAllCandidates(String socialId, Long groupId) throws GroupNotFoundException, NotValidMemberException {
    boolean isValidatedMember = groupService.checkGroupMembers(socialId, groupId);
    if (!isValidatedMember) {
      throw new NotValidMemberException(BaseResponseStatus.NOT_VALID_USER);
    }
    List<GroupWhereViewResponseDto> candidates = groupWhereQueryRepository.findAllByGroupId(groupId);
    return Optional.ofNullable(candidates);
  }

  public GroupWhereViewResponseDto createCandidate(GroupWhereCreateRequestDto request, String socialId, Long groupId) throws NotValidMemberException, GroupNotFoundException {
    Group groupFound = groupRepository.findById(groupId)
            .orElseThrow(() -> new GroupNotFoundException(BaseResponseStatus.NOT_EXIST_GROUPID));
    boolean isValidatedMember = groupService.checkGroupMembers(socialId, groupId);
    if (!isValidatedMember) {
      throw new NotValidMemberException(BaseResponseStatus.NOT_VALID_USER);
    }

    GroupWhere groupWhere = GroupWhere.builder()
            .groupWhereName(request.getGroupLocationName())
            .groupWhereUrl(request.getGroupWhereUrl())
            .group(groupFound)
            .build();

    groupWhereRepository.save(groupWhere);

    // GroupWhereViewResponseDto 반환
    return GroupWhereViewResponseDto.builder()
            .groupLocationName(groupWhere.getGroupWhereName())
            .groupWhereUrl(groupWhere.getGroupWhereUrl())
            .count(groupWhere.getCount())
            .build();
  }

  public GroupWhereViewResponseDto voteCandidate(String socialId, Long groupId, Long groupWhereId) throws GroupNotFoundException, NotValidMemberException {
    Group groupFound = groupRepository.findById(groupId)
            .orElseThrow(() -> new GroupNotFoundException(BaseResponseStatus.NOT_EXIST_GROUPID));
    boolean isValidatedMember = groupService.checkGroupMembers(socialId, groupId);
    if (!isValidatedMember) {
      throw new NotValidMemberException(BaseResponseStatus.NOT_VALID_USER);
    }
    

  }
}
