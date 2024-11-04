package timetogether.domain.calendar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import timetogether.domain.calendar.exception.CalendarValidateFail;
import timetogether.domain.where2meet.Where2meet;
import timetogether.domain.calendar.dto.request.CalendarCreateRequestDto;
import timetogether.domain.calendar.dto.request.CalendarUpdateRequestDto;
import timetogether.domain.calendar.dto.response.CalendarCreateResponseDto;
import timetogether.domain.calendar.dto.response.CalendarUpdateResponseDto;
import timetogether.domain.calendar.exception.CalendarNotExist;
import timetogether.domain.meeting.Meeting;
import timetogether.domain.meeting.repository.MeetingRepository;
import timetogether.domain.where2meet.repository.Where2meetRepository;
import timetogether.global.response.BaseResponseStatus;
import timetogether.oauth2.entity.User;
import timetogether.oauth2.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CalendarService {
  private final MeetingRepository meetingRepository;
  private final Where2meetRepository where2meetRepository;
  private final UserRepository userRepository;


  public CalendarCreateResponseDto createMeeting(String socialId,CalendarCreateRequestDto request) throws CalendarNotExist, CalendarValidateFail {
    User matchedUser = userRepository.findBySocialId(socialId)
            .orElseThrow(() -> new CalendarNotExist(BaseResponseStatus.NOT_VALID_USER));

    //유효성 검증
    validateCalendarCreateInput(request);

    Where2meet newWhere = new Where2meet(request.getLocationName(), request.getLocationUrl());
    where2meetRepository.save(newWhere);

    //새로운 Meeting 클래스 만들기h = {JdkDynamicAopProxy@14269}
    Meeting newMeeting = Meeting.builder()
            .meetTitle(request.getMeetTitle())
            .meetContent(request.getMeetContent())
            .meetType(request.getMeetType())
            .meetDTstart(request.getMeetDTstart())
            .meetDTend(request.getMeetDTend())
            .groupName(request.getGroupName())
            .calendar(matchedUser.getCalendar())
            .where2meet(newWhere)
            .build();

    meetingRepository.save(newMeeting);

    return CalendarCreateResponseDto.builder()
            .meetTitle(newMeeting.getMeetTitle())
            .meetContent(newMeeting.getMeetContent())
            .meetType(newMeeting.getMeetType())
            .meetDTstart(newMeeting.getMeetDTstart())
            .meetDTend(newMeeting.getMeetDTend())
            .groupName(newMeeting.getGroupName())
            .locationName(newMeeting.getWhere2meet().getLocationName())
            .locationUrl(newMeeting.getWhere2meet().getLocationUrl())
            .build();
  }

  private void validateCalendarCreateInput(CalendarCreateRequestDto calendarCreateRequestDto) throws CalendarValidateFail {
    LocalDateTime startTime = calendarCreateRequestDto.getMeetDTstart();
    LocalDateTime endTime = calendarCreateRequestDto.getMeetDTend();

    // startTimer과 endTime null값 검사
    if (startTime == null || endTime == null) {
      throw new CalendarValidateFail(BaseResponseStatus.NOT_VALID_CALENDAR_TIME_NULL);
    }

    // startTime가 endTimeqh보다 전인지 검사
    if (startTime.isAfter(endTime)) {
      throw new CalendarValidateFail(BaseResponseStatus.NOT_VALID_CALENDAR_STARTTIME_AFTER_ENDTIME);
    }

  }

  //여기 수정중!!
  public CalendarUpdateResponseDto updateMeeting(String socialId, CalendarUpdateRequestDto request) throws CalendarNotExist {
    User matchedUser = userRepository.findBySocialId(socialId)
            .orElseThrow(() -> new CalendarNotExist(BaseResponseStatus.NOT_VALID_USER));
//    //Meeting 클래스 가져오기
//    meetingRepository.findAllById()
//    Meeting newMeeting = Meeting.builder()
//            .meetDTstart(year + "-" + month + date)
//            .meetDTend(request.getMeetDTend())
//            .meetTitle(request.getMeetTitle())
//            .meetContent(request.getMeetContent())
//            .groupName(request.getGroupName())
//            .calendar(matchedUser.getCalendar())
//            .build();
//
//    meetingRepository.save(newMeeting);
//
//    return calendarUpdateResponseDto.builder()
//            .meetDTstart(newMeeting.getMeetDTstart())
//            .meetDTend(newMeeting.getMeetDTend())
//            .meetTitle(newMeeting.getMeetTitle())
//            .meetContent(newMeeting.getMeetContent())
//            .groupName(newMeeting.getGroupName())
//            .build();
    return null;
  }
}
