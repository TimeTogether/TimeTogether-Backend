package timetogether.group.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import timetogether.global.response.BaseResponse;
import timetogether.global.response.BaseResponseService;
import timetogether.global.response.BaseResponseStatus;
import timetogether.group.dto.GroupCreateRequestDto;
import timetogether.group.dto.GroupCreateResponseDto;
import timetogether.group.dto.GroupLeaveResponseDto;
import timetogether.group.dto.GroupShowResponseDto;
import timetogether.group.exception.GroupNotFoundException;
import timetogether.group.exception.NotAllowedGroupMgrToLeave;
import timetogether.group.exception.NotGroupMgrInGroup;
import timetogether.group.exception.NotValidMemberException;
import timetogether.group.service.GroupService;
import timetogether.jwt.service.JwtService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupController {

  private final BaseResponseService baseResponseService;
  private final GroupService groupService;
  private final JwtService jwtService;

  /**
   * 그룹생성
   *
   * @param headerRequest
   * @return
   */
  @PostMapping("/create")
  public BaseResponse<Object> createGroup(
          HttpServletRequest headerRequest,
          @RequestBody GroupCreateRequestDto request
  ) {
    Optional<String> accessToken = jwtService.extractAccessToken(headerRequest);
    Optional<String> socialId = jwtService.extractId(accessToken.get());
    GroupCreateResponseDto groupCreateResponseDto = groupService.createGroup(socialId.get(), request);
    return baseResponseService.getSuccessResponse(groupCreateResponseDto);
  }

  /**
   * 초대 코드 보기
   *
   * @param headerRequest
   * @param groupId
   * @return
   * @throws GroupNotFoundException
   */
  @GetMapping("/invitationCode/{groupId}")
  public BaseResponse<Object> getInvitationCode(
          HttpServletRequest headerRequest,
          @PathVariable("groupId") Long groupId
  ) throws GroupNotFoundException {
    Optional<String> accessToken = jwtService.extractAccessToken(headerRequest);
    Optional<String> socialId = jwtService.extractId(accessToken.get());
    boolean checkIfMemberInGroup = groupService.checkGroupMembers(socialId.get(), groupId);
    if (checkIfMemberInGroup){
      String invitationCode = groupService.getInvitationCode(groupId);
      return baseResponseService.getSuccessResponse(invitationCode);
    }else{
      return baseResponseService.getFailureResponse(BaseResponseStatus.NOT_VALID_USER);
    }
  }

  @PostMapping("/invited/{groupUrl}")
  public BaseResponse<Object> getIntoGroupByInvitationCode(
          HttpServletRequest headerRequest,
          @PathVariable("groupUrl") String groupUrl
  ) throws GroupNotFoundException, NotValidMemberException {
    Optional<String> accessToken = jwtService.extractAccessToken(headerRequest);
    Optional<String> socialId = jwtService.extractId(accessToken.get());
    Long groupId = groupService.getGroupIdFromCode(groupUrl);
    boolean checkIfMemberInGroup = groupService.checkGroupMembers(socialId.get(), groupId);
    if (checkIfMemberInGroup){//이미 그룹에 등록된 경우
      return baseResponseService.getFailureResponse(BaseResponseStatus.ALREADY_EXIST_IN_GROUP);
    }else{//그룹에 등록되지 않은 경우
      groupService.getIntoGroup(socialId.get(),groupId);
      return baseResponseService.getSuccessResponse(BaseResponseStatus.SUCCESS);
    }
  }

  /**
   * 방장이 그룹 삭제
   *
   * @param headerRequest
   * @param groupId
   * @return
   * @throws GroupNotFoundException
   * @throws NotGroupMgrInGroup
   */
  @DeleteMapping("/delete/{groupId}")
  public BaseResponse<Object> deleteGroup(
          HttpServletRequest headerRequest,
          @PathVariable("groupId") Long groupId
  ) throws GroupNotFoundException, NotGroupMgrInGroup {
    Optional<String> accessToken = jwtService.extractAccessToken(headerRequest);
    Optional<String> socialId = jwtService.extractId(accessToken.get());

    String deletedGroup = groupService.deleteGroup(socialId.get(),groupId);
    return baseResponseService.getSuccessResponse(deletedGroup);
  }


  /**
   * 그룹 떠나기 (방장 제외)
   *
   * @param headerRequest
   * @param groupId
   * @return
   * @throws GroupNotFoundException
   * @throws NotValidMemberException
   */
  @DeleteMapping("/leave/{groupId}")
  public BaseResponse<Object> leaveGroup(
          HttpServletRequest headerRequest,
          @PathVariable("groupId") Long groupId
  ) throws GroupNotFoundException, NotValidMemberException, NotAllowedGroupMgrToLeave {
    Optional<String> accessToken = jwtService.extractAccessToken(headerRequest);
    Optional<String> socialId = jwtService.extractId(accessToken.get());

    GroupLeaveResponseDto groupLeaveResponseDto = groupService.leaveGroup(socialId.get(),groupId);
    return baseResponseService.getSuccessResponse(groupLeaveResponseDto);
  }

  /**
   * 속한 그룹 전체 조회
   *
   * @param headerRequest
   * @return
   */
  @GetMapping("/groups/view")
  public BaseResponse<Object> showGroupsWhereUserIn(
          HttpServletRequest headerRequest
  ) {
    Optional<String> accessToken = jwtService.extractAccessToken(headerRequest);
    Optional<String> socialId = jwtService.extractId(accessToken.get());

    List<GroupShowResponseDto> groupShowResponseDtoList = groupService.showGroupsWhereSocialIdIn(socialId.get());
    //List<GroupShowResponseDto> groupShowResponseDtoList = groupService.showGroupsWhereSocialIdIn(socialId);
    groupShowResponseDtoList.stream()
            .forEach(e -> e.addIsMgr(socialId.get()));
    return baseResponseService.getSuccessResponse(groupShowResponseDtoList);
  }



//  /**
//   * 그룹 날짜 설정
//   *
//   * @param headerRequest
//   * @param request
//   * @return
//   * @throws GroupTimesLimitSevenDays
//   * @throws GroupNotFoundOrNotMgrException
//   */
//  @PostMapping("/addDates")
//  public BaseResponse<Object> createDates(
//          HttpServletRequest headerRequest,
//          @RequestBody GroupAddDatesRequestDto request
//  ) throws GroupTimesLimitSevenDays, GroupNotFoundOrNotMgrException {
//    Optional<String> accessToken = jwtService.extractAccessToken(headerRequest);
//    Optional<String> socialId = jwtService.extractId(accessToken.get());
//    GroupAddDatesResponseDto groupAddDatesResponseDto = groupService.addDates(socialId.get(),request);
//    return baseResponseService.getSuccessResponse(groupAddDatesResponseDto);
//  }
////  /**
////   * 그룹 정보 수정
////   *
////   * @param headerRequest
////   * @param groupId
////   * @param request
////   * @return
////   */
////  @PatchMapping("/edit/{groupId}")
////  public BaseResponse<Object> updateGroup(
////          HttpServletRequest headerRequest,
////          @PathVariable("groupId") Long groupId,
////          @RequestBody GroupUpdateRequestDto request
////  ) throws NotValidMemberException, GroupNotFoundException, NotGroupMgrInGroup {
////    log.info("group edit 시작");
////    Optional<String> accessToken = jwtService.extractAccessToken(headerRequest);
////    Optional<String> socialId = jwtService.extractId(accessToken.get());
////
////    GroupUpdateResponseDto groupUpdateResponseDto = groupService.editGroup(socialId.get(), groupId,request);
////    return baseResponseService.getSuccessResponse(groupUpdateResponseDto);
////  }
//

//
//
}
