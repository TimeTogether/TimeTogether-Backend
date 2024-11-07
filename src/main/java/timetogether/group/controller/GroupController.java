package timetogether.group.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import timetogether.calendar.exception.CalendarNotExist;
import timetogether.calendar.exception.CalendarValidateFail;
import timetogether.group.dto.GroupCreateRequestDto;
import timetogether.group.dto.GroupCreateResponseDto;
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
   * @param socialId
   * @param groupId
   * @param request
   * @return
   */
  @PostMapping("/{groupId}/edit")
  public BaseResponse<Object> createGroup(
          @RequestParam(value = "socialId") String socialId, //임시
          @PathVariable(value = "groupId") Long groupId,
          @RequestBody GroupCreateRequestDto request
  ) {
    GroupCreateResponseDto groupCreateResponseDto = groupService.editGroup(socialId, groupId,request);
    return baseResponseService.getSuccessResponse(groupCreateResponseDto);
  }
}
