package timetogether.group.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import timetogether.calendar.exception.CalendarNotExist;
import timetogether.calendar.exception.CalendarValidateFail;
import timetogether.group.dto.GroupCreateRequestDto;
import timetogether.group.dto.GroupCreateResponseDto;
import timetogether.group.service.GroupService;
import timetogether.global.response.BaseResponse;
import timetogether.global.response.BaseResponseService;

@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupController {

  private BaseResponseService baseResponseService;
  private GroupService groupService;

  /**
   * 그룹 생성
   *
   * @param socialId
   * @param request
   * @return
   */
  @PostMapping("/create")
  public BaseResponse<Object> createGroup(
          @RequestParam(value = "socialId") String socialId, //임시
          @RequestBody GroupCreateRequestDto request
  ) {
    GroupCreateResponseDto groupCreateResponseDto = groupService.createGroup(socialId, request);
    return baseResponseService.getSuccessResponse(groupCreateResponseDto);
  }
  @PostMapping("/{groupId}/edit")
  public BaseResponse<Object> createGroup(
          @RequestParam(value = "socialId") String socialId, //임시
          @RequestParam(value = "groupId") Long groupId,
          @RequestBody GroupCreateRequestDto request
  ) {
    GroupCreateResponseDto groupCreateResponseDto = groupService.editGroup(socialId, groupId,request);
    return baseResponseService.getSuccessResponse(groupCreateResponseDto);
  }
}
