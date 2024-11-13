package timetogether.when2meet.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import timetogether.group.Group;
import timetogether.group.repository.GroupProjection;
import timetogether.group.repository.GroupRepository;
import timetogether.meeting.Meeting;
import timetogether.meeting.repository.MeetingRepository;
import timetogether.oauth2.entity.User;
import timetogether.oauth2.repository.UserRepository;
import timetogether.when2meet.When2meet;
import timetogether.when2meet.dto.Result;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@AllArgsConstructor
public class When2MeetService {

    private final GroupRepository groupRepository;
    private final MeetingRepository meetingRepository;
    public List<Result> view(Long groupId) {
        List<Result> resultList = new LinkedList<>();
        GroupProjection groupNameById = groupRepository.findGroupNameById(groupId);
        String groupName = groupNameById.getGroupName(); // group service에 더 적합

        Optional<List<Meeting>> byGroupName = meetingRepository.findByGroupName(groupName);
        List<Meeting> meeting = byGroupName.get(); // meet service에 더 적합

        for(Meeting meet : meeting){
            resultList.add(new Result(meet.getId(), meet.getMeetDTstart(),
                    meet.getMeetDTend(), meet.getMeetType(),
                    meet.getMeetTitle(), groupName,
                    meet.getWhere2meet().getLocationName(), meet.getWhere2meet().getLocationUrl()));
        }

        return resultList;
    }

    public void addGroupMeet(String date, Long groupId) {
        Optional<Group> optional = groupRepository.findById(groupId);
        Group group = optional.get();
        List<When2meet> when2meetList = group.getWhen2meetList();

        String day = LocalDate.parse(date).getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN);
        //when2meetList.add(new When2meet(date, day, group.getMeetType(), ,group))
    }
}
