package timetogether.where2meet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timetogether.global.response.BaseResponseStatus;
import timetogether.group.Group;
import timetogether.group.exception.GroupNotFoundException;
import timetogether.group.exception.NotGroupMgrInGroup;
import timetogether.group.exception.NotValidMemberException;
import timetogether.group.repository.GroupRepository;
import timetogether.group.service.GroupService;
import timetogether.GroupWhere.GroupWhere;
import timetogether.where2meet.dto.GroupWhereChooseResponse;
import timetogether.where2meet.dto.GroupWhereCreateRequestDto;
import timetogether.where2meet.dto.GroupWhereViewResponseDto;
import timetogether.where2meet.exception.GroupWhereNotFoundException;
import timetogether.where2meet.repository.GroupWhereQueryRepository;
import timetogether.where2meet.repository.GroupWhereRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class Where2meetService {
  private final GroupRepository groupRepository;
  private final GroupService groupService;
  private final GroupWhereQueryRepository groupWhereQueryRepository;
  private final GroupWhereRepository groupWhereRepository;


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

  public GroupWhereViewResponseDto voteCandidate(String socialId, Long groupId, Long groupWhereId) throws GroupNotFoundException, NotValidMemberException, GroupWhereNotFoundException {
    Group groupFound = groupRepository.findById(groupId)
            .orElseThrow(() -> new GroupNotFoundException(BaseResponseStatus.NOT_EXIST_GROUPID));
    boolean isValidatedMember = groupService.checkGroupMembers(socialId, groupId);
    if (!isValidatedMember) {
      throw new NotValidMemberException(BaseResponseStatus.NOT_VALID_USER);
    }
    GroupWhere groupWhereFound = groupWhereRepository.findById(groupWhereId)
            .orElseThrow(() -> new GroupWhereNotFoundException(BaseResponseStatus.NOT_EXIST_GROUPWHERE));

    groupWhereFound.addCount();
    return GroupWhereViewResponseDto.builder()
            .groupLocationName(groupWhereFound.getGroupWhereName())
            .groupWhereUrl(groupWhereFound.getGroupWhereUrl())
            .count(groupWhereFound.getCount())
            .build();
  }

  public String delete(String socialId, Long groupId, Long groupWhereId) throws GroupNotFoundException, NotValidMemberException, GroupWhereNotFoundException {
    Group groupFound = groupRepository.findById(groupId)
            .orElseThrow(() -> new GroupNotFoundException(BaseResponseStatus.NOT_EXIST_GROUPID));
    boolean isValidatedMember = groupService.checkGroupMembers(socialId, groupId);
    if (!isValidatedMember) {
      throw new NotValidMemberException(BaseResponseStatus.NOT_VALID_USER);
    }
    GroupWhere groupWhereFound = groupWhereRepository.findById(groupWhereId)
            .orElseThrow(() -> new GroupWhereNotFoundException(BaseResponseStatus.NOT_EXIST_GROUPWHERE));

    groupWhereRepository.delete(groupWhereFound);
    return "삭제가 완료되었습니다.";
  }

  public GroupWhereChooseResponse doneGroupWhere(String socialId, Long groupId, Long groupWhereId) throws GroupNotFoundException, NotGroupMgrInGroup, GroupWhereNotFoundException {
    Group groupFound = groupRepository.findById(groupId)
            .orElseThrow(() -> new GroupNotFoundException(BaseResponseStatus.NOT_EXIST_GROUPID));
    boolean isMgr = groupFound.getGroupMgrId().equals(socialId); //방장인 경우 판별
    if (!isMgr){
      throw new NotGroupMgrInGroup(BaseResponseStatus.NOT_VALID_MGR);
    }
    GroupWhere groupWhereFound = groupWhereRepository.findById(groupWhereId)
            .orElseThrow(() -> new GroupWhereNotFoundException(BaseResponseStatus.NOT_EXIST_GROUPWHERE));
    groupWhereFound.doneChooseThis();
    return GroupWhereChooseResponse.builder()
            .groupLocationName(groupWhereFound.getGroupWhereName())
            .groupWhereUrl(groupWhereFound.getGroupWhereUrl())
            .build();

  }
}
