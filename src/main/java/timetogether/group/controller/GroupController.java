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

  @PostMapping("/create")
  public BaseResponse<Object> createGroup(
          @RequestParam(value = "socialId") String socialId, //임시
          @RequestBody GroupCreateRequestDto request
  ) throws CalendarNotExist, CalendarValidateFail {
    //방장인지 확인 여부 로직 필요

    GroupCreateResponseDto groupCreateResponseDto = groupService.createGroup(socialId, request);
    return baseResponseService.getSuccessResponse(groupCreateResponseDto);
  }
}
