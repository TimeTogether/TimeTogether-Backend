package timetogether.meeting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import timetogether.group.Group;
<<<<<<< HEAD
import timetogether.group.repository.GroupRepository;
import timetogether.meeting.Meeting;
=======
>>>>>>> master
import timetogether.meeting.repository.MeetingRepository;
import timetogether.oauth2.entity.User;
import timetogether.oauth2.repository.UserRepository;
import timetogether.when2meet.dto.Result;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
<<<<<<< HEAD
    private final GroupRepository groupRepository;

    public List<Result> getMeetByGroup(String socialId, Long groupId) {
        String groupName = groupRepository.findGroupNameById(groupId).getGroupName();
        List<Meeting> meeting = meetingRepository.findByGroupName(groupName); // meet service에 더 적합
        List<Result> resultList = new LinkedList<>();
        for(Meeting meet : meeting){
            resultList.add(new Result(meet.getMeetId(), meet.getMeetDTstart(),
                    meet.getMeetDTend(), meet.getMeetType(),
                    meet.getMeetTitle(), groupName,
                    meet.getWhere2meet().getLocationName(), meet.getWhere2meet().getLocationUrl()));
        }
        return resultList;
    }

    public List<Result> getMeet(String socialId) {
=======
    public void getMeetByGroup(String socialId) {
>>>>>>> master
        User user = userRepository.findBySocialId(socialId).get();
        List<Group> groupList = user.getGroupList();
        List<Result> resultList = new LinkedList<>();

        for(Group group:groupList){
            String groupName = group.getGroupName();
            List<Meeting> meeting = meetingRepository.findByGroupName(groupName); // meet service에 더 적합
            for(Meeting meet : meeting){
                resultList.add(new Result(meet.getMeetId(), meet.getMeetDTstart(),
                        meet.getMeetDTend(), meet.getMeetType(),
                        meet.getMeetTitle(), groupName,
                        meet.getWhere2meet().getLocationName(), meet.getWhere2meet().getLocationUrl()));
            }
        }
        return resultList;
    }

    public List<Result> searchMeet(String socialId, Long groupId, String title) {
        String groupName = groupRepository.findGroupNameById(groupId).getGroupName();
        List<Meeting> meeting = meetingRepository.findByGroupName(groupName);
        List<Result> resultList = new LinkedList<>();

        for(Meeting meet : meeting){
            if(meet.getMeetTitle().equals(title)) { // title과 같을때
                resultList.add(new Result(meet.getMeetId(), meet.getMeetDTstart(),
                        meet.getMeetDTend(), meet.getMeetType(),
                        meet.getMeetTitle(), groupName,
                        meet.getWhere2meet().getLocationName(), meet.getWhere2meet().getLocationUrl()));
            }
        }
        return resultList;
    }

}
