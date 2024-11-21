package timetogether.calendar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timetogether.calendar.exception.CalendarValidateFail;
import timetogether.calendar.exception.InCalendarMeetingIdNotExist;
import timetogether.where2meet.Where2meet;
import timetogether.calendar.dto.request.CalendarCreateRequestDto;
import timetogether.calendar.dto.request.CalendarUpdateRequestDto;
import timetogether.calendar.dto.response.CalendarCreateResponseDto;
import timetogether.calendar.dto.response.CalendarUpdateResponseDto;
import timetogether.calendar.exception.CalendarNotExist;
import timetogether.meeting.Meeting;
import timetogether.meeting.repository.MeetingRepository;
import timetogether.where2meet.repository.Where2meetQueryRepository;
import timetogether.where2meet.repository.Where2meetRepository;
import timetogether.global.response.BaseResponseStatus;
import timetogether.oauth2.entity.User;
import timetogether.oauth2.repository.UserRepository;

import java.time.LocalDateTime;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Service
@RequiredArgsConstructor
@Transactional
public class CalendarService {
  private final MeetingRepository meetingRepository;
  private final Where2meetRepository where2meetRepository;
  private final UserRepository userRepository;
  private final Where2meetQueryRepository where2meetQueryRepository;


  public CalendarCreateResponseDto createMeeting(String socialId,CalendarCreateRequestDto request) throws CalendarNotExist, CalendarValidateFail {
    User matchedUser = userRepository.findBySocialId(socialId)
            .orElseThrow(() -> new CalendarNotExist(BaseResponseStatus.NOT_VALID_USER));

    //유효성 검증
    validateCalendarCreateInput(request);
    log.info("유효성 검증 통과");
    Where2meet newWhere = new Where2meet(request.getLocationName(), request.getLocationUrl());
    where2meetRepository.save(newWhere);
    log.info("새로운 장소 추가 완료");
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

    Meeting save = meetingRepository.save(newMeeting);
    log.info("새로운 일정 추가 완료");
    return CalendarCreateResponseDto.builder()
            .meetingId(save.getMeetId())
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

  //나중에 리팩토링 할 예정
  private void validateCalendarCreateInput(CalendarCreateRequestDto calendarCreateRequestDto) throws CalendarValidateFail {
    LocalDateTime startTime = calendarCreateRequestDto.getMeetDTstart();
    LocalDateTime endTime = calendarCreateRequestDto.getMeetDTend();

    // startTime과 endTime null값 검사
    if (startTime == null || endTime == null) {
      throw new CalendarValidateFail(BaseResponseStatus.NOT_VALID_CALENDAR_TIME_NULL);
    }

    // startTime가 endTimeqh보다 전인지 검사
    if (startTime.isAfter(endTime)) {
      throw new CalendarValidateFail(BaseResponseStatus.NOT_VALID_CALENDAR_STARTTIME_AFTER_ENDTIME);
    }

  }

  private void validateCalendarCreateInput2(CalendarUpdateRequestDto calendarUpdateRequestDto) throws CalendarValidateFail {
    LocalDateTime startTime = calendarUpdateRequestDto.getMeetDTstart();
    LocalDateTime endTime = calendarUpdateRequestDto.getMeetDTend();

    // startTimer과 endTime null값 검사
    if (startTime == null || endTime == null) {
      throw new CalendarValidateFail(BaseResponseStatus.NOT_VALID_CALENDAR_TIME_NULL);
    }

    // startTime가 endTimeqh보다 전인지 검사
    if (startTime.isAfter(endTime)) {
      throw new CalendarValidateFail(BaseResponseStatus.NOT_VALID_CALENDAR_STARTTIME_AFTER_ENDTIME);
    }

  }

  public CalendarUpdateResponseDto updateMeeting(String socialId, Long meetingId, CalendarUpdateRequestDto request) throws CalendarNotExist, CalendarValidateFail, InCalendarMeetingIdNotExist {
    User matchedUser = userRepository.findBySocialId(socialId)
            .orElseThrow(() -> new CalendarNotExist(BaseResponseStatus.NOT_VALID_USER));

    //유효성 검증
    validateCalendarCreateInput2(request);

    //기존 meeting 객체 Where2meet null로 바꾸기


    //기존 location 가져오기
    Long locationId = where2meetQueryRepository.deleteByLocationIdInMeetingId(meetingId);

    //새로운 location 저장
    Where2meet newWhere = new Where2meet(request.getLocationName(), request.getLocationUrl());
    where2meetRepository.save(newWhere);

    // 기존 meeting 객체 가져오기
    Meeting existMeeting = meetingRepository.findById(meetingId)
            .orElseThrow(() -> new InCalendarMeetingIdNotExist(BaseResponseStatus.NOT_EXIST_MEETINGID));

    // 기존 meeting 객체 업데이트
    existMeeting.update(request,newWhere);

    // 새로운 meeting 객체로 바꾸기
    Meeting save = meetingRepository.save(existMeeting);

    // 기존 location 삭제
    where2meetRepository.deleteById(locationId);

    return CalendarUpdateResponseDto.builder()
            .meetingId(save.getMeetId())
            .meetTitle(existMeeting.getMeetTitle())
            .meetContent(existMeeting.getMeetContent())
            .meetType(existMeeting.getMeetType())
            .meetDTstart(existMeeting.getMeetDTstart())
            .meetDTend(existMeeting.getMeetDTend())
            .groupName(existMeeting.getGroupName())
            .locationName(existMeeting.getWhere2meet().getLocationName())
            .locationUrl(existMeeting.getWhere2meet().getLocationUrl())
            .build();
  }

  public void deleteMeeting(Long meetingId) throws InCalendarMeetingIdNotExist {

    // 기존 meeting 객체 가져오기
    Meeting existMeeting = meetingRepository.findById(meetingId)
            .orElseThrow(() -> new InCalendarMeetingIdNotExist(BaseResponseStatus.NOT_EXIST_MEETINGID));

    where2meetRepository.deleteById(existMeeting.getWhere2meet().getLocationId());
    meetingRepository.delete(existMeeting);
  }
}
