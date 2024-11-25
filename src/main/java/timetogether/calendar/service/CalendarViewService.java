package timetogether.calendar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timetogether.calendar.Calendar;
import timetogether.calendar.dto.response.CalendarMergeResponseDto;
import timetogether.calendar.dto.response.CalendarWhere2meetDto;
import timetogether.calendar.dto.response.CalendarViewResponseDto;
import timetogether.calendar.exception.CalendarNotExist;
import timetogether.calendar.repository.CalendarRepository;
import timetogether.global.response.BaseResponseStatus;
import timetogether.groupMeeting.MeetType;
import timetogether.meeting.Meeting;
import timetogether.meeting.dto.response.MeetingResponseDto;
import timetogether.meeting.repository.MeetingRepository;
import timetogether.oauth2.entity.User;
import timetogether.oauth2.repository.UserRepository;
import timetogether.where2meet.Where2meet;
import timetogether.where2meet.repository.Where2meetQueryRepository;
import timetogether.where2meet.repository.Where2meetRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalendarViewService {
  private final CalendarRepository calendarRepository;
  private final UserRepository userRepository;
  private final Where2meetRepository where2meetRepository;
  private final Where2meetQueryRepository where2meetQueryRepository;
  private final MeetingRepository meetingRepository;

  /**
   * 현재 년, 월에 등록된 전체 일정 리스트 DTO로 반환
   *
   * @return List<CalendarRepository>
   */
  public CalendarViewResponseDto getMeetingsYearMonth(Long calendarId, int year, int month) {
    return calendarRepository.findMeetings(calendarId,year, month);
  }
  public CalendarViewResponseDto getMeetingsYearMonthDate(Long calendarId, int year, int month, int date) {
    CalendarViewResponseDto result =  calendarRepository.findMeetings(calendarId,year, month,date);
    result.addDate(date);
    return result;
  }

  @Transactional
  public Long putandGetCalendarId(String socialId) throws CalendarNotExist {
    User matchedUser = userRepository.findBySocialId(socialId)
            .orElseThrow(() -> new CalendarNotExist(BaseResponseStatus.NOT_VALID_USER));
    if (matchedUser.getCalendar() != null) {//이미 calendarId가 존재하는 경우 반환
      return matchedUser.getCalendar().getCalendarId();
    }
    Calendar savedCalendar = calendarRepository.save(new Calendar());
    matchedUser.updateCalendarId(savedCalendar);
    userRepository.save(matchedUser);
    return savedCalendar.getCalendarId();
  }


  public CalendarViewResponseDto getMeeting(Long calendarId, Long meetingId) {
    CalendarViewResponseDto result =  calendarRepository.findMeetings(calendarId,meetingId);
    result.addDate(result.getDate());
    return result;
  }

  public List<CalendarWhere2meetDto> getWhere2meetInMeetings(CalendarViewResponseDto calendarViewResponseDto) {
    List<CalendarWhere2meetDto> where2meetList = new ArrayList<>(); // 빈 리스트로 초기화
    List<MeetingResponseDto> meetingList = calendarViewResponseDto.getMeetingList();

    if (meetingList == null || meetingList.isEmpty()) {
      return where2meetList;
    }

    for (MeetingResponseDto meetingResponseDto : meetingList) {
      Meeting foundMeeting = meetingRepository.findById(meetingResponseDto.getId()).get();
      if (foundMeeting.getMeetType() != null) {


        if (foundMeeting.getMeetType().equals(MeetType.ONLINE)) {//온라인인 경우
          where2meetList.add(new CalendarWhere2meetDto(
                  meetingResponseDto.getId(),
                  null,
                  null
          ));
        } else {//오프라인인 경우
          Long foundWhere2meetId = foundMeeting.getWhere2meet().getLocationId();
          Where2meet foudnWhere2meet = where2meetQueryRepository.findById(foundWhere2meetId);

          where2meetList.add(new CalendarWhere2meetDto(
                  meetingResponseDto.getId(),
                  foudnWhere2meet.getLocationName(),
                  foudnWhere2meet.getLocationUrl()
          ));
        }
      }
      //meetType 이 null 인 경우
      Long foundWhere2meetId = foundMeeting.getWhere2meet().getLocationId();
      Where2meet foudnWhere2meet = where2meetQueryRepository.findById(foundWhere2meetId);

      where2meetList.add(new CalendarWhere2meetDto(
              meetingResponseDto.getId(),
              foudnWhere2meet.getLocationName(),
              foudnWhere2meet.getLocationUrl()
      ));

    }

    return where2meetList;
  }

  public CalendarMergeResponseDto merge(CalendarViewResponseDto calendarViewResponseDto, List<CalendarWhere2meetDto> calendarWhere2meetDto) {
    return new CalendarMergeResponseDto(calendarViewResponseDto, calendarWhere2meetDto);
  }
}