package timetogether.GroupWhere.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import timetogether.GroupWhere.dto.GroupWhereChooseResponse;
import timetogether.GroupWhere.dto.GroupWhereCreateRequestDto;
import timetogether.GroupWhere.dto.GroupWhereViewResponseDto;
import timetogether.GroupWhere.service.GroupWhereService;
import timetogether.global.response.BaseResponse;
import timetogether.global.response.BaseResponseService;
import timetogether.group.exception.GroupNotFoundException;
import timetogether.group.exception.NotGroupMgrInGroup;
import timetogether.group.exception.NotValidMemberException;
import timetogether.jwt.service.JwtService;
import timetogether.GroupWhere.exception.GroupWhereNotFoundException;
import timetogether.meeting.service.MeetingService;
import timetogether.where2meet.service.Where2meetService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupWhereController {
  private final BaseResponseService baseResponseService;
  private final GroupWhereService groupWhereService;
  private final JwtService jwtService;
  private final Where2meetService where2meetService;
  private final MeetingService meetingService;

  /**
   * 후보 장소 전체 조회
   *
   * @param headerRequest
   * @param groupId
   * @return
   * @throws GroupNotFoundException
   * @throws NotValidMemberException
   */
  @GetMapping("/{groupId}/{groupMeetingId}/where/view")
  public BaseResponse<Object> viewAllCandidates(
          HttpServletRequest headerRequest,
          @PathVariable("groupId") Long groupId,
          @PathVariable("groupMeetingId") Long groupMeetingId
  ) throws GroupNotFoundException, NotValidMemberException {
    Optional<String> accessToken = jwtService.extractAccessToken(headerRequest);
    Optional<String> socialId = jwtService.extractId(accessToken.get());
    Optional<List<GroupWhereViewResponseDto>> groupViewResponseList = groupWhereService.getAllCandidates(socialId.get(), groupId, groupMeetingId);
    return baseResponseService.getSuccessResponse(groupViewResponseList);
  }


  /**
   * 후보에 장소 등록
   *
   * @param headerRequest
   * @param groupId
   * @param request
   * @return
   * @throws GroupNotFoundException
   * @throws NotValidMemberException
   */
  @PostMapping("/{groupId}/{groupMeetingId}/where/create")
  public BaseResponse<Object> createCandidate(
          HttpServletRequest headerRequest,
          @PathVariable("groupId") Long groupId,
          @PathVariable("groupMeetingId") Long groupMeetingId,
          @RequestBody GroupWhereCreateRequestDto request
  ) throws GroupNotFoundException, NotValidMemberException {
    Optional<String> accessToken = jwtService.extractAccessToken(headerRequest);
    Optional<String> socialId = jwtService.extractId(accessToken.get());
    GroupWhereViewResponseDto groupViewResponse = groupWhereService.createCandidate(request, socialId.get(), groupId, groupMeetingId);
    return baseResponseService.getSuccessResponse(groupViewResponse);
  }

  /**
   * 후보 장소 투표
   *
   * @param headerRequest
   * @param groupId
   * @param groupWhereId
   * @return
   * @throws GroupNotFoundException
   * @throws NotValidMemberException
   */
  @PostMapping("/{groupId}/{groupMeetingId}/where/vote/{groupWhereId}")
  public BaseResponse<Object> voteCandidate(
          HttpServletRequest headerRequest,
          @PathVariable("groupId") Long groupId,
          @PathVariable("groupWhereId") Long groupWhereId,
          @PathVariable("groupMeetingId") Long groupMeetingId
  ) throws GroupNotFoundException, NotValidMemberException, GroupWhereNotFoundException {
    Optional<String> accessToken = jwtService.extractAccessToken(headerRequest);
    Optional<String> socialId = jwtService.extractId(accessToken.get());
    GroupWhereViewResponseDto groupViewResponse = groupWhereService.voteCandidate(socialId.get(), groupId, groupMeetingId, groupWhereId);
    return baseResponseService.getSuccessResponse(groupViewResponse);
  }

  /**
   * 그룹 특정 후보 장소 삭제
   *
   * @param headerRequest
   * @param groupId
   * @param groupWhereId
   * @return
   * @throws GroupNotFoundException
   * @throws NotValidMemberException
   */
  @DeleteMapping("/{groupId}/{groupMeetingId}/where/delete/{groupWhereId}")
  public BaseResponse<Object> deleteCandidate(
          HttpServletRequest headerRequest,
          @PathVariable("groupId") Long groupId,
          @PathVariable("groupMeetingId") Long groupMeetingId,
          @PathVariable("groupWhereId") Long groupWhereId
  ) throws GroupNotFoundException, NotValidMemberException, GroupWhereNotFoundException {
    Optional<String> accessToken = jwtService.extractAccessToken(headerRequest);
    Optional<String> socialId = jwtService.extractId(accessToken.get());
    String response = groupWhereService.delete(socialId.get(), groupId, groupMeetingId, groupWhereId);
    return baseResponseService.getSuccessResponse(response);
  }

  @PostMapping("/{groupId}/{groupMeetingId}/where/done/{groupWhereId}")
  public BaseResponse<Object> doneGroupWhere(
          HttpServletRequest headerRequest,
          @PathVariable("groupId") Long groupId,
          @PathVariable("groupMeetingId") Long groupMeetingId,
          @PathVariable("groupWhereId") Long groupWhereId
  ) throws GroupNotFoundException, NotGroupMgrInGroup, GroupWhereNotFoundException {
    Optional<String> accessToken = jwtService.extractAccessToken(headerRequest);
    Optional<String> socialId = jwtService.extractId(accessToken.get());
    GroupWhereChooseResponse done = groupWhereService.doneGroupWhere(socialId.get(), groupId, groupMeetingId, groupWhereId);
    //where2meetService.create(done);
    return baseResponseService.getSuccessResponse(done);
  }



}