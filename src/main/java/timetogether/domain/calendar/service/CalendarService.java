package timetogether.domain.calendar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import timetogether.domain.Where2meet;
import timetogether.domain.calendar.dto.request.CalendarCreateRequestDto;
import timetogether.domain.calendar.dto.request.CalendarUpdateRequestDto;
import timetogether.domain.calendar.dto.response.CalendarCreateResponseDto;
import timetogether.domain.calendar.dto.response.CalendarUpdateResponseDto;
import timetogether.domain.calendar.exception.CalendarNotExist;
import timetogether.domain.meeting.Meeting;
import timetogether.domain.meeting.repository.MeetingRepository;
import timetogether.global.response.BaseResponseStatus;
import timetogether.oauth2.entity.User;
import timetogether.oauth2.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CalendarService {
  private final MeetingRepository meetingRepository;
  private final UserRepository userRepository;


  public CalendarCreateResponseDto createMeeting(String socialId,CalendarCreateRequestDto request) throws CalendarNotExist {
    User matchedUser = userRepository.findBySocialId(socialId)
            .orElseThrow(() -> new CalendarNotExist(BaseResponseStatus.NOT_VALID_USER));
    //새로운 Meeting 클래스 만들기
    Meeting newMeeting = Meeting.builder()
            .meetTitle(request.getMeetTitle())
            .meetContent(request.getMeetContent())
            .meetType(request.getMeetType())
            .meetDTstart(request.getMeetDTstart())
            .meetDTend(request.getMeetDTend())
            .groupName(request.getGroupName())
            .calendar(matchedUser.getCalendar())
            .build();

    meetingRepository.save(newMeeting);

    return CalendarCreateResponseDto.builder()
            .meetDTstart(newMeeting.getMeetDTstart())
            .meetDTend(newMeeting.getMeetDTend())
            .meetTitle(newMeeting.getMeetTitle())
            .meetContent(newMeeting.getMeetContent())
            .groupName(newMeeting.getGroupName())
            .build();
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
