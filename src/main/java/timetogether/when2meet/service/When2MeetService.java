package timetogether.when2meet.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import timetogether.group.Group;
import timetogether.group.repository.GroupProjection;
import timetogether.group.repository.GroupRepository;
import timetogether.meeting.Meeting;
import timetogether.meeting.repository.MeetingRepository;
import timetogether.oauth2.entity.User;
import timetogether.oauth2.repository.UserProjection;
import timetogether.oauth2.repository.UserRepository;
import timetogether.ranktime.RankTime;
import timetogether.when2meet.When2meet;
import timetogether.when2meet.dto.Days;
import timetogether.when2meet.dto.Result;
import timetogether.when2meet.repository.When2MeetRepository;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

@Service
@AllArgsConstructor
public class When2MeetService {

    private final GroupRepository groupRepository;
    private final MeetingRepository meetingRepository;
    private final When2MeetRepository when2MeetRepository;
    private final  UserRepository userRepository;
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

    private List<String> dates = new ArrayList<>();
    public void addGroupMeet(String socialId, List<String> dates, Long groupId) {
        this.dates = dates; // date를 저장하고 있기
        Group group = groupRepository.findById(groupId).get();
        String groupTimes = groupRepository.findGroupTimesById(groupId).getGroupTimes(); // 우선순위 테이블을 위함
        User user =  userRepository.findBySocialId(socialId).get();

        for(String date : dates) {
            String day = LocalDate.parse(date).getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN); // 요일

            When2meet when2meet = new When2meet(date, day, group.getMeetType(), user, group);
            RankTime rankTime = new RankTime(when2meet, group, user, groupTimes, groupTimes); // 우선순위 테이블 초기 설정

            user.addWhen2meet(when2meet); // 사용자의 when2meet 업데이트
            group.addWhen2meet(when2meet); // 그룹의 when2meet 업데이트

            when2MeetRepository.save(when2meet);
        }
        userRepository.save(user); // 해당 user를 찾아서 업데이트
        groupRepository.save(group); // 해당 group을 찾아서 업데이트
    }

    public void viewMeet(String socialId, Long groupId) {
        GroupProjection groupTimesById = groupRepository.findGroupTimesById(groupId);
        String groupTimes = groupTimesById.getGroupTimes();
        List<String> members = groupRepository.findById(groupId).get().parserGroupMembers();

        UserProjection userNameBySocialId = userRepository.findUserNameBySocialId(socialId);
        String userName = userNameBySocialId.getUserName();

        List<Days> days = new ArrayList<>();
        for(String date : dates){
            When2meet when2meet = when2MeetRepository.findByDate(date).get();

        }

        days.add(new Days())



    }
}
