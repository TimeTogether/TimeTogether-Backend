package timetogether.GroupWhere.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timetogether.GroupWhere.GroupWhere;
import timetogether.GroupWhere.dto.GroupWhereChooseResponse;
import timetogether.GroupWhere.dto.GroupWhereCreateRequestDto;
import timetogether.GroupWhere.dto.GroupWhereViewResponseDto;
import timetogether.GroupWhere.repository.GroupWhereQueryRepository;
import timetogether.config.kakaoapi.KakaoAPI;
import timetogether.global.response.BaseResponseStatus;
import timetogether.group.Group;
import timetogether.group.exception.GroupNotFoundException;
import timetogether.group.exception.NotGroupMgrInGroup;
import timetogether.group.exception.NotValidMemberException;
import timetogether.group.repository.GroupRepository;
import timetogether.group.service.GroupService;
import timetogether.groupMeeting.GroupMeeting;
import timetogether.groupMeeting.repository.GroupMeetingRepository;
import timetogether.GroupWhere.exception.GroupWhereNotFoundException;
import timetogether.GroupWhere.repository.GroupWhereRepository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class GroupWhereService {
  private static KakaoAPI kakaoApi = new KakaoAPI();
  private final GroupRepository groupRepository;
  private final GroupService groupService;
  private final GroupWhereQueryRepository groupWhereQueryRepository;
  private final GroupWhereRepository groupWhereRepository;
  private final GroupMeetingRepository groupMeetingRepository;

  public GroupWhereViewResponseDto createCandidate(GroupWhereCreateRequestDto request, String socialId, Long groupId, Long groupMeetingId) throws NotValidMemberException, GroupNotFoundException {
    Group groupFound = groupRepository.findById(groupId)
            .orElseThrow(() -> new GroupNotFoundException(BaseResponseStatus.NOT_EXIST_GROUPID));
    boolean isValidatedMember = groupService.checkGroupMembers(socialId, groupId);
    if (!isValidatedMember) {
      throw new NotValidMemberException(BaseResponseStatus.NOT_VALID_USER);
    }
    GroupMeeting groupMeeting = groupMeetingRepository.findById(groupMeetingId).get();
    GroupWhere groupWhere = GroupWhere.builder()
            .groupWhereName(request.getGroupWhereName())
            .groupWhereUrl(request.getGroupWhereUrl())
            .group(groupFound)
            .groupMeeting(groupMeeting)
            .build();

    GroupWhere save = groupWhereRepository.save(groupWhere);

    // GroupWhereViewResponseDto 반환
    return GroupWhereViewResponseDto.builder()
            .groupWhereId(save.getId())
            .groupId(save.getGroup().getId())
            .groupLocationName(save.getGroupWhereName())
            .groupWhereUrl(save.getGroupWhereUrl())
            .count(save.getCount())
            .groupMeetingId(save.getGroupMeeting().getGroupMeetId())
            .build();
  }


  public Optional<List<GroupWhereViewResponseDto>> getAllCandidates(String socialId, Long groupId, Long groupMeetingId) throws GroupNotFoundException, NotValidMemberException {
    boolean isValidatedMember = groupService.checkGroupMembers(socialId, groupId);
    if (!isValidatedMember) {
      throw new NotValidMemberException(BaseResponseStatus.NOT_VALID_USER);
    }
    List<GroupWhereViewResponseDto> candidates = groupWhereQueryRepository.findAllByGroupIdAndGroupMeetingId(groupId,groupMeetingId);
    return Optional.ofNullable(candidates);
  }

  public GroupWhereViewResponseDto voteCandidate(String socialId, Long groupId, Long groupMeetingId, Long groupWhereId, Long upAndDown) throws GroupNotFoundException, NotValidMemberException, GroupWhereNotFoundException {
    Group groupFound = groupRepository.findById(groupId)
            .orElseThrow(() -> new GroupNotFoundException(BaseResponseStatus.NOT_EXIST_GROUPID));
    boolean isValidatedMember = groupService.checkGroupMembers(socialId, groupId);
    if (!isValidatedMember) {
      throw new NotValidMemberException(BaseResponseStatus.NOT_VALID_USER);
    }
    GroupWhere groupWhereFound = groupWhereRepository.findById(groupWhereId)
            .orElseThrow(() -> new GroupWhereNotFoundException(BaseResponseStatus.NOT_EXIST_GROUPWHERE));

    groupWhereFound.changeCount(upAndDown); //투표

    groupWhereRepository.save(groupWhereFound);

    return GroupWhereViewResponseDto.builder()
            .groupWhereId(groupWhereFound.getId())
            .groupId(groupWhereFound.getGroup().getId())
            .groupLocationName(groupWhereFound.getGroupWhereName())
            .groupWhereUrl(groupWhereFound.getGroupWhereUrl())
            .count(groupWhereFound.getCount())
            .groupMeetingId(groupMeetingId)
            .build();
  }

  public String delete(String socialId, Long groupId, Long groupMeetingId , Long groupWhereId) throws GroupNotFoundException, NotValidMemberException, GroupWhereNotFoundException {
    Group groupFound = groupRepository.findById(groupId)
            .orElseThrow(() -> new GroupNotFoundException(BaseResponseStatus.NOT_EXIST_GROUPID));
    boolean isValidatedMember = groupService.checkGroupMembers(socialId, groupId);
    log.info("validated");
    if (!isValidatedMember) {
      throw new NotValidMemberException(BaseResponseStatus.NOT_VALID_USER);
    }
    log.info("validated success");
    log.info("groupWhereId = {}", groupWhereId);
    GroupWhere groupWhereFound = groupWhereRepository.findById(groupWhereId)
            .orElseThrow(() -> new GroupWhereNotFoundException(BaseResponseStatus.NOT_EXIST_GROUPWHERE));

    groupWhereRepository.delete(groupWhereFound);
    log.info("delete success!");
    return "삭제가 완료되었습니다.";
  }


  public GroupWhereChooseResponse doneGroupWhere(String socialId, Long groupId, Long groupMeetingId, Long groupWhereId) throws GroupNotFoundException, NotGroupMgrInGroup, GroupWhereNotFoundException {
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
            .groupWhereId(groupWhereFound.getId())
            .groupId(groupWhereFound.getGroup().getId())
            .groupWhereName(groupWhereFound.getGroupWhereName())
            .groupWhereUrl(groupWhereFound.getGroupWhereUrl())
            .groupWhereUrl(groupWhereFound.getGroupWhereUrl())
            .groupWhereChooseThis(groupWhereFound.isChooseThis())
            .build();

  }

  public void getRandomGroupWhereCandidates(Long groupId, String groupMeetingTitle) throws IOException, URISyntaxException {
    try {
      kakaoApi.keywordSearch("건국대학교");
      kakaoApi.setRadius(1000);
      List<GroupWhere> locations = kakaoApi.categorySearch();
      //주변 장소 후보 데이터베이스에 저장
      for (GroupWhere candidate : locations){
        Group groupFound = groupRepository.findById(groupId).get();
        GroupMeeting groupMeetingFound = groupMeetingRepository.findByGroupAndGroupMeetingTitle(groupFound,groupMeetingTitle);

        candidate.settingGroup(groupFound);
        candidate.settingGroupMeeting(groupMeetingFound);
        groupWhereRepository.save(candidate);
      }
    } catch (Exception e) {
      log.error("장소 검색 중 오류 발생: {}", e.getMessage());
      throw e;
    }
  }
}
