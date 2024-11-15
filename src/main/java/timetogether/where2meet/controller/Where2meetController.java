package timetogether.where2meet.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import timetogether.global.response.BaseResponse;
import timetogether.global.response.BaseResponseService;
import timetogether.group.exception.GroupNotFoundException;
import timetogether.group.exception.NotGroupMgrInGroup;
import timetogether.group.exception.NotValidMemberException;
import timetogether.jwt.service.JwtService;
import timetogether.where2meet.dto.GroupWhereChooseResponse;
import timetogether.where2meet.dto.GroupWhereCreateRequestDto;
import timetogether.where2meet.dto.GroupWhereViewResponseDto;
import timetogether.where2meet.exception.GroupWhereNotFoundException;
import timetogether.where2meet.service.Where2meetService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class Where2meetController {

//  private final BaseResponseService baseResponseService;
//  private final Where2meetService where2meetService;
//  private final JwtService jwtService;
//
//  /**
//   * 후보 장소 전체 조회
//   *
//   * @param headerRequest
//   * @param groupId
//   * @return
//   * @throws GroupNotFoundException
//   * @throws NotValidMemberException
//   */
//  @GetMapping("/{groupId}/where/view")
//  public BaseResponse<Object> viewAllCandidates(
//          HttpServletRequest headerRequest,
//          @PathVariable("groupId") Long groupId
//  ) throws GroupNotFoundException, NotValidMemberException {
//    Optional<String> accessToken = jwtService.extractAccessToken(headerRequest);
//    Optional<String> socialId = jwtService.extractId(accessToken.get());
//    Optional<List<GroupWhereViewResponseDto>> groupViewResponseList = where2meetService.getAllCandidates(socialId.get(), groupId);
//    return baseResponseService.getSuccessResponse(groupViewResponseList);
//  }
//
//  /**
//   * 후보에 장소 등록
//   *
//   * @param headerRequest
//   * @param groupId
//   * @param request
//   * @return
//   * @throws GroupNotFoundException
//   * @throws NotValidMemberException
//   */
//  @PostMapping("/{groupId}/where/create")
//  public BaseResponse<Object> createCandidate(
//          HttpServletRequest headerRequest,
//          @PathVariable("groupId") Long groupId,
//          @RequestBody GroupWhereCreateRequestDto request
//  ) throws GroupNotFoundException, NotValidMemberException {
//    Optional<String> accessToken = jwtService.extractAccessToken(headerRequest);
//    Optional<String> socialId = jwtService.extractId(accessToken.get());
//    GroupWhereViewResponseDto groupViewResponse = where2meetService.createCandidate(request, socialId.get(), groupId);
//    return baseResponseService.getSuccessResponse(groupViewResponse);
//  }
//
//  /**
//   * 후보 장소 투표
//   *
//   * @param headerRequest
//   * @param groupId
//   * @param groupWhereId
//   * @return
//   * @throws GroupNotFoundException
//   * @throws NotValidMemberException
//   */
//  @PostMapping("/{groupId}/where/vote/{groupWhereId}")
//  public BaseResponse<Object> voteCandidtate(
//          HttpServletRequest headerRequest,
//          @PathVariable("groupId") Long groupId,
//          @PathVariable("groupWhereId") Long groupWhereId
//  ) throws GroupNotFoundException, NotValidMemberException, GroupWhereNotFoundException {
//    Optional<String> accessToken = jwtService.extractAccessToken(headerRequest);
//    Optional<String> socialId = jwtService.extractId(accessToken.get());
//    GroupWhereViewResponseDto groupViewResponse = where2meetService.voteCandidate(socialId.get(), groupId, groupWhereId);
//    return baseResponseService.getSuccessResponse(groupViewResponse);
//  }
//
//  /**
//   * 그룹 특정 후보 장소 삭제
//   *
//   * @param headerRequest
//   * @param groupId
//   * @param groupWhereId
//   * @return
//   * @throws GroupNotFoundException
//   * @throws NotValidMemberException
//   */
//  @DeleteMapping("{groupId}/where/delete/{groupWhereId}")
//  public BaseResponse<Object> deleteCandidate(
//          HttpServletRequest headerRequest,
//          @PathVariable("groupId") Long groupId,
//          @PathVariable("groupWhereId") Long groupWhereId
//  ) throws GroupNotFoundException, NotValidMemberException, GroupWhereNotFoundException {
//    Optional<String> accessToken = jwtService.extractAccessToken(headerRequest);
//    Optional<String> socialId = jwtService.extractId(accessToken.get());
//    String response = where2meetService.delete(socialId.get(), groupId, groupWhereId);
//    return baseResponseService.getSuccessResponse(response);
//  }
//
//  @PostMapping("/{groupId}/where/done/{groupWhereId}")
//  public BaseResponse<Object> doneGroupWhere(
//          HttpServletRequest headerRequest,
//          @PathVariable("groupId") Long groupId,
//          @PathVariable("groupWhereId") Long groupWhereId
//  ) throws GroupNotFoundException, NotGroupMgrInGroup, GroupWhereNotFoundException {
//    Optional<String> accessToken = jwtService.extractAccessToken(headerRequest);
//    Optional<String> socialId = jwtService.extractId(accessToken.get());
//    GroupWhereChooseResponse done = where2meetService.doneGroupWhere(socialId.get(), groupId, groupWhereId);
//    return baseResponseService.getSuccessResponse(done);
//  }
}