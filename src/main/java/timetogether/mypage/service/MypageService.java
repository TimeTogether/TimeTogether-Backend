package timetogether.mypage.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import timetogether.calendar.Calendar;
import timetogether.meeting.Meeting;
import timetogether.meeting.repository.MeetingRepository;
import timetogether.oauth2.entity.User;
import timetogether.oauth2.repository.UserRepository;
import timetogether.when2meet.dto.Result;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
@AllArgsConstructor
public class MypageService {

    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    public List<Result> findMeetByUser(String socialId) {
        User user = userRepository.findBySocialId(socialId).get();
        Calendar calendar = user.getCalendar();
        LocalDateTime now = LocalDateTime.now();

        List<Meeting> meeting = meetingRepository.findPastMeetings(calendar.getCalendarId(), now);
        List<Result> resultList = new LinkedList<>();
        for(Meeting meet : meeting){
            if(meet.getWhere2meet() != null) {
                resultList.add(new Result(meet.getMeetId(), meet.getMeetDTstart(),
                        meet.getMeetDTend(), meet.getMeetType(),
                        meet.getMeetTitle(), meet.getGroupName(),
                        meet.getWhere2meet().getLocationName(), meet.getWhere2meet().getLocationUrl()));
            }else{
                resultList.add(new Result(meet.getMeetId(), meet.getMeetDTstart(),
                        meet.getMeetDTend(), meet.getMeetType(),
                        meet.getMeetTitle(), meet.getGroupName()));
            }
        }
        return resultList;
    }

    public List<Result> searchMeetByUser(String socialId, String title) {
        User user = userRepository.findBySocialId(socialId).get();
        Calendar calendar = user.getCalendar();
        LocalDateTime now = LocalDateTime.now();

        List<Meeting> meeting = meetingRepository.findPastMeetings(calendar.getCalendarId(), now);
        List<Result> resultList = new LinkedList<>();
        for(Meeting meet : meeting){
            if(meet.getMeetTitle().equals(title)) {
                if (meet.getWhere2meet() != null) {
                    resultList.add(new Result(meet.getMeetId(), meet.getMeetDTstart(),
                            meet.getMeetDTend(), meet.getMeetType(),
                            meet.getMeetTitle(), meet.getGroupName(),
                            meet.getWhere2meet().getLocationName(), meet.getWhere2meet().getLocationUrl()));
                } else {
                    resultList.add(new Result(meet.getMeetId(), meet.getMeetDTstart(),
                            meet.getMeetDTend(), meet.getMeetType(),
                            meet.getMeetTitle(), meet.getGroupName()));

                }
            }
        }
        return resultList;
    }
}
