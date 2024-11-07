package timetogether.group.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import timetogether.calendar.exception.CalendarNotExist;
import timetogether.calendar.exception.CalendarValidateFail;
import timetogether.group.dto.GroupCreateRequestDto;
import timetogether.group.dto.GroupCreateResponseDto;
import timetogether.group.dto.GroupUpdateRequestDto;
import timetogether.group.dto.GroupUpdateResponseDto;
import timetogether.group.exception.GroupNotFoundException;
import timetogether.group.exception.NotGroupMgrInGroup;
import timetogether.group.exception.NotValidMembersException;
import timetogether.group.service.GroupService;
import timetogether.global.response.BaseResponse;
import timetogether.global.response.BaseResponseService;
import timetogether.jwt.service.JwtService;

import java.util.Optional;

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
   * @throws NotValidMembersException
   */
  @PostMapping("/create")
  public BaseResponse<Object> createGroup(
          HttpServletRequest headerRequest,
          @RequestBody GroupCreateRequestDto request
  ) throws NotValidMembersException {
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
  @PostMapping("/{groupId}/edit")
  public BaseResponse<Object> updateGroup(
          HttpServletRequest headerRequest,
          @PathVariable(value = "groupId") Long groupId,
          @RequestBody GroupUpdateRequestDto request
  ) throws NotValidMembersException, GroupNotFoundException, NotGroupMgrInGroup {
    Optional<String> accessToken = jwtService.extractAccessToken(headerRequest);
    Optional<String> socialId = jwtService.extractId(accessToken.get());

    GroupUpdateResponseDto groupUpdateResponseDto = groupService.editGroup(socialId.get(),groupId,request);
    return baseResponseService.getSuccessResponse(groupUpdateResponseDto);
  }

  @DeleteMapping("/{groupId}/delete")
  public BaseResponse<Object> deleteGroup(
          HttpServletRequest headerRequest,
          @PathVariable(value = "groupId") Long groupId
  ) throws NotValidMembersException, GroupNotFoundException, NotGroupMgrInGroup {
    Optional<String> accessToken = jwtService.extractAccessToken(headerRequest);
    Optional<String> socialId = jwtService.extractId(accessToken.get());

    String deletedGroup = groupService.deleteGroup(socialId.get(),groupId);
    return baseResponseService.getSuccessResponse(deletedGroup);
  }

  @
}
