package timetogether.domain.calendar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import timetogether.domain.calendar.dto.request.CalendarCreateRequestDto;
import timetogether.domain.calendar.dto.response.CalendarCreateResponseDto;
import timetogether.domain.calendar.repository.CalendarRepository;

@Service
@RequiredArgsConstructor
public class CalendarService {
  private final CalendarRepository calendarRepository;


  public CalendarCreateResponseDto createMeeting(Long calendarId, int year, int month, int date, CalendarCreateRequestDto request) {

    return null; //여기 구현하는 중!
  }
}
