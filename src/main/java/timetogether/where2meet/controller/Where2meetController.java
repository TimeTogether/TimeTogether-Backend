package timetogether.where2meet.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class Where2meetController {

//  private final BaseResponseService baseResponseService;
//  private final Where2meetService where2meetService;
//  private final JwtService jwtService;
//

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