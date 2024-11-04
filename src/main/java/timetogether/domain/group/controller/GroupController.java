package timetogether.domain.group.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import timetogether.domain.calendar.dto.request.CalendarCreateRequestDto;
import timetogether.domain.calendar.dto.response.CalendarCreateResponseDto;
import timetogether.domain.calendar.exception.CalendarNotExist;
import timetogether.domain.calendar.exception.CalendarValidateFail;
import timetogether.domain.group.dto.GroupCreateRequestDto;
import timetogether.domain.group.dto.GroupCreateResponseDto;
import timetogether.domain.group.service.GroupService;
import timetogether.global.response.BaseResponse;
import timetogether.global.response.BaseResponseService;
import timetogether.global.response.BaseResponseStatus;

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
