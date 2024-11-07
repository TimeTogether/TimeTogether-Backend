package timetogether.group.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import timetogether.calendar.exception.CalendarNotExist;
import timetogether.calendar.exception.CalendarValidateFail;
import timetogether.group.dto.*;
import timetogether.group.exception.GroupNotFoundException;
import timetogether.group.exception.NotAllowedGroupMgrToLeave;
import timetogether.group.exception.NotGroupMgrInGroup;
import timetogether.group.exception.NotValidMemberException;
import timetogether.group.service.GroupService;
import timetogether.global.response.BaseResponse;
import timetogether.global.response.BaseResponseService;
import timetogether.jwt.service.JwtService;

import java.util.Optional;

import static org.hibernate.query.sqm.tree.SqmNode.log;

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
   * @throws NotValidMemberException
   */
  @PostMapping("/create")
  public BaseResponse<Object> createGroup(
          HttpServletRequest headerRequest,
          @RequestBody GroupCreateRequestDto request
  ) throws NotValidMemberException {
    Optional<String> accessToken = jwtService.extractAccessToken(headerRequest);
    Optional<String> socialId = jwtService.extractId(accessToken.get());

    GroupCreateResponseDto groupCreateResponseDto = groupService.createGroup(socialId.get(), request);
    return baseResponseService.getSuccessResponse(groupCreateResponseDto);
  }

  /**
   * 그룹 정보 수정
   *
   * @param headerRequest
   * @param groupId
   * @param request
   * @return
   */
  @PatchMapping("/edit/{groupId}")
  public BaseResponse<Object> updateGroup(
          HttpServletRequest headerRequest,
          @PathVariable("groupId") Long groupId,
          @RequestBody GroupUpdateRequestDto request
  ) throws NotValidMemberException, GroupNotFoundException, NotGroupMgrInGroup {
    log.info("group edit 시작");
    Optional<String> accessToken = jwtService.extractAccessToken(headerRequest);
    Optional<String> socialId = jwtService.extractId(accessToken.get());

    GroupUpdateResponseDto groupUpdateResponseDto = groupService.editGroup(socialId.get(), groupId,request);
    return baseResponseService.getSuccessResponse(groupUpdateResponseDto);
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
}
