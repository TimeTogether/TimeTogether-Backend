package timetogether.meeting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import timetogether.calendar.Calendar;
import timetogether.group.Group;
import timetogether.group.repository.GroupRepository;
import timetogether.meeting.Meeting;
import timetogether.meeting.repository.MeetingRepository;
import timetogether.oauth2.entity.User;
import timetogether.oauth2.repository.UserRepository;
import timetogether.when2meet.dto.Result;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final GroupRepository groupRepository;

    public List<Result> getMeetByGroup(String socialId, Long groupId) {
        User user = userRepository.findBySocialId(socialId).get();
        Calendar calendar = user.getCalendar();
        LocalDateTime now = LocalDateTime.now();
        String groupName = groupRepository.findGroupNameById(groupId).getGroupName();

        List<Meeting> meeting = meetingRepository.findUpcomingMeetings(calendar.getCalendarId(), now);
        List<Result> resultList = new LinkedList<>();
        for(Meeting meet : meeting){
            if(meet.getWhere2meet() != null) {
                resultList.add(new Result(meet.getMeetId(), meet.getMeetDTstart(),
                        meet.getMeetDTend(), meet.getMeetType(),
                        meet.getMeetTitle(), groupName,
                        meet.getWhere2meet().getLocationName(), meet.getWhere2meet().getLocationUrl()));
            }else{
                resultList.add(new Result(meet.getMeetId(), meet.getMeetDTstart(),
                        meet.getMeetDTend(), meet.getMeetType(),
                        meet.getMeetTitle(), groupName));
            }
        }
        return resultList;
    }

    public List<Result> getMeet(String socialId) {
        User user = userRepository.findBySocialId(socialId).get();
        Calendar calendar = user.getCalendar();
        LocalDateTime now = LocalDateTime.now();
        List<Group> groupList = user.getGroupList();
        List<Result> resultList = new LinkedList<>();

        for(Group group:groupList){
            String groupName = group.getGroupName();
            List<Meeting> meeting = meetingRepository.findUpcomingMeetings(calendar.getCalendarId(), now);
            for(Meeting meet : meeting){
                //TODO: 중복코드수정
                if(meet.getWhere2meet() != null) {
                    resultList.add(new Result(meet.getMeetId(), meet.getMeetDTstart(),
                            meet.getMeetDTend(), meet.getMeetType(),
                            meet.getMeetTitle(), groupName,
                            meet.getWhere2meet().getLocationName(), meet.getWhere2meet().getLocationUrl()));
                }else{
                    resultList.add(new Result(meet.getMeetId(), meet.getMeetDTstart(),
                            meet.getMeetDTend(), meet.getMeetType(),
                            meet.getMeetTitle(), groupName));
                }
            }
        }
        return resultList;
    }

    public List<Result> searchMeet(String socialId, Long groupId, String title) {
        String groupName = groupRepository.findGroupNameById(groupId).getGroupName();
        Calendar calendar = userRepository.findBySocialId(socialId).get().getCalendar();
        LocalDateTime now = LocalDateTime.now();
        List<Meeting> meeting = meetingRepository.findUpcomingMeetings(calendar.getCalendarId(), now);
        List<Result> resultList = new LinkedList<>();

        for(Meeting meet : meeting){
            if(meet.getMeetTitle().equals(title)) { // title과 같을때
                if(meet.getWhere2meet() != null) {
                    resultList.add(new Result(meet.getMeetId(), meet.getMeetDTstart(),
                            meet.getMeetDTend(), meet.getMeetType(),
                            meet.getMeetTitle(), groupName,
                            meet.getWhere2meet().getLocationName(), meet.getWhere2meet().getLocationUrl()));
                }else{
                    resultList.add(new Result(meet.getMeetId(), meet.getMeetDTstart(),
                            meet.getMeetDTend(), meet.getMeetType(),
                            meet.getMeetTitle(), groupName));
                }
            }
        }
        return resultList;
    }

}
