package timetogether.when2meet.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import timetogether.group.Group;
import timetogether.group.repository.GroupProjection;
import timetogether.group.repository.GroupRepository;
import timetogether.meeting.MeetType;
import timetogether.meeting.Meeting;
import timetogether.meeting.repository.MeetingRepository;
import timetogether.oauth2.entity.User;
import timetogether.oauth2.repository.UserRepository;
import timetogether.ranktime.RankTime;
import timetogether.ranktime.RankTimeRepository;
import timetogether.when2meet.When2meet;
import timetogether.when2meet.dto.Days;
import timetogether.when2meet.dto.GroupTableDTO;
import timetogether.when2meet.dto.Result;
import timetogether.when2meet.dto.Users;
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
    private final RankTimeRepository rankTimeRepository;
    private final UserRepository userRepository;

    public List<Result> viewMeetResult(Long groupId) {
        List<Result> resultList = new LinkedList<>();
        GroupProjection groupNameById = groupRepository.findGroupNameById(groupId);
        String groupName = groupNameById.getGroupName(); // group service에 더 적합

        Optional<List<Meeting>> byGroupName = meetingRepository.findByGroupName(groupName);
        List<Meeting> meeting = byGroupName.get(); // meet service에 더 적합

        for (Meeting meet : meeting) {
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
        User user = userRepository.findBySocialId(socialId).get();

        // groupId로 해당 그룹에 user를 모두 가져온 후에, 해당 user에 대해서 when2meet, ranktime을 생성

        // 모든 사용자에 대해서 해당 date의 when2meet과 ranktime 테이블을 생성한 후에, 저장해야한다

        for (String date : dates) {
            // 회의 일정을 추가할때 동일한 날짜에 대해서 접근할 수 없다
            String day = LocalDate.parse(date).getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN); // 요일

            When2meet when2meetOff = new When2meet(date, day, MeetType.OFFLINE, user, group);
            When2meet when2meetOn = new When2meet(date, day, MeetType.ONLINE, user, group);
            RankTime rankTimeOff = new RankTime(when2meetOff, group, user, groupTimes.length(), groupTimes.length()); // 우선순위 테이블 초기 설정
            RankTime rankTimeOn = new RankTime(when2meetOn, group, user, groupTimes.length(), groupTimes.length()); // 우선순위 테이블 초기 설정

            user.addWhen2meet(when2meetOff); // 사용자의 when2meet 업데이트 offline
//            group.addWhen2meet(when2meetOff); // 그룹의 when2meet 업데이트
            user.addWhen2meet(when2meetOn); // 사용자의 when2meet 업데이트 online
//            group.addWhen2meet(when2meetOn); // 그룹의 when2meet 업데이트

            when2MeetRepository.save(when2meetOff);
            when2MeetRepository.save(when2meetOn);

            rankTimeRepository.save(rankTimeOff);
            rankTimeRepository.save(rankTimeOn);
        }
        userRepository.save(user); // 해당 user를 찾아서 업데이트
//        groupRepository.save(group); // 해당 group을 찾아서 업데이트
    }

    public GroupTableDTO viewMeet(Long groupId, MeetType type) {
        String groupTimes = groupRepository.findGroupTimesById(groupId).getGroupTimes();
        List<String> members = groupRepository.findById(groupId).get().parserGroupMembers();
        List<Users> users = new ArrayList<>();

        for (String socialId : members) {
            User user = userRepository.findBySocialId(socialId).get();

            List<Days> days = new ArrayList<>();
            for (String date : dates) {
                When2meet when2meet = when2MeetRepository.findByDateAndUserAndType(date, user, type).get(); // date와 user로 when찾기
                // 온라인 오프라인은 되어있음
                String day = when2meet.getDay();

                RankTime rankTime = rankTimeRepository.findByWhen2meet(when2meet); // when2meet으로 rankTime찾기

                String time = rankTime.getTime();
                String rank = rankTime.getRank();
                days.add(new Days(date, day, time, rank));
            }
            users.add(new Users(user.getUserName(), days));
        }

        return new GroupTableDTO(groupTimes, type, users);
    }

    public GroupTableDTO addUserMeet(Long groupId, MeetType type, String userName) {
        // 사용자는 (그룹, 회의 ID, TYPE을 확인한 후) when2meet 틀을 만든다 즉, 초기화

        // when2meet (date day meetType user group) : date = meetId에 포함된 date들을 받는다
        // rankTime (when2meet, group, user, groupTimes.length(), groupTimes.length())

        // when2meet 저장
        // rankTime 저장

        // user 저장
        // group 저장

        //return new GroupTableDTO(groupTimes, type, days);
        return null;
    }

    public GroupTableDTO<Days> loadUserMeet(Long groupId, MeetType meetType, String userNames) {
        return null;
    }

    public void updateUserMeet(Long groupId, MeetType meetType, String userNames) {
    }

    public void doneGroupMeet(Long groupId, MeetType meetType, String userNames) {
    }
}
