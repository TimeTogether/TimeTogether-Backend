package timetogether.when2meet.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import timetogether.calendar.Calendar;
import timetogether.group.Group;
import timetogether.group.repository.GroupProjection;
import timetogether.group.repository.GroupRepository;
import timetogether.groupMeeting.GroupMeeting;
import timetogether.groupMeeting.GroupMeetingRepository;
import timetogether.groupMeeting.MeetType;
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
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class When2MeetService {

    private final GroupRepository groupRepository;
    private final MeetingRepository meetingRepository;
    private final When2MeetRepository when2MeetRepository;
    private final RankTimeRepository rankTimeRepository;
    private final UserRepository userRepository;
    private final GroupMeetingRepository groupMeetingRepository;

    public List<Result> viewMeetResult(Long groupId) {
        List<Result> resultList = new LinkedList<>();
        GroupProjection groupNameById = groupRepository.findGroupNameById(groupId);
        String groupName = groupNameById.getGroupName(); // group service에 더 적합

        Optional<List<Meeting>> byGroupName = meetingRepository.findByGroupName(groupName);
        List<Meeting> meeting = byGroupName.get(); // meet service에 더 적합

        for (Meeting meet : meeting) {
            resultList.add(new Result(meet.getMeetId(), meet.getMeetDTstart(),
                    meet.getMeetDTend(), meet.getMeetType(),
                    meet.getMeetTitle(), groupName,
                    meet.getWhere2meet().getLocationName(), meet.getWhere2meet().getLocationUrl()));
        }

        return resultList;
    }

    private List<String> dates = new ArrayList<>();
    public void addGroupMeet(String groupMeetingTitle, List<String> dates, Long groupId) {
        Group group = groupRepository.findById(groupId).get(); // 그룹 아이디로 그룹을 조회
        List<User> users = group.getUserList(); // 그룹에서 사용자 리스트 조회

        List<Long> meetId = initGroupMeeting(users, group, groupMeetingTitle);// 회의 일정 초기화, 모든 사용자에 대해 회의 일정 추가
        initWhen2Meet(group, meetId, dates); // 특정 날짜 초기화, 모든 사용자에 대해 특정 날짜 추가

        this.dates = dates; // 다른 방식 찾기
    }

    private void initWhen2Meet(Group group, List<Long> meetId, List<String> dates) {
        for(int i=0; i<meetId.size(); i++) {
            for(String date : dates){
                String day = LocalDate.parse(date).getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN); // 요일
                GroupMeeting groupMeeting = groupMeetingRepository.findById(meetId.get(i)).get(); // 회의 일정

                When2meet offline = new When2meet(date, day, group, groupMeeting, MeetType.OFFLINE);
                When2meet online = new When2meet(date, day, group, groupMeeting, MeetType.ONLINE);

                when2MeetRepository.save(offline);
                when2MeetRepository.save(online);
            }
        }
    }

    private List<Long> initGroupMeeting(List<User> users, Group group, String groupMeetingTitle) {
        List<GroupMeeting> meetings = new ArrayList<>();
        List<Long> meetId = new ArrayList<>();

        for(int i=0; i<users.size(); i++){
            GroupMeeting groupMeeting = new GroupMeeting(groupMeetingTitle, group, users.get(i)); // meetId를 하나로 고정
            users.get(i).initGroupMeeting(groupMeeting); // 여러 사용자에 대해 같은 meetId로 접근할 수 있도록

            meetings.add(groupMeeting);
            meetId.add(groupMeeting.getGroupMeetId()); // meetId 저장
        }

        userRepository.saveAll(users);
        groupMeetingRepository.saveAll(meetings);
        return meetId;
    }

    public GroupTableDTO viewMeet(Long groupId, String groupMeetingTitle, MeetType type) {
        Group group = groupRepository.findById(groupId).get();
        String groupTimes = group.getGroupTimes();
        List<GroupMeeting> meetings = groupMeetingRepository.findByGroupMeetingTitle(groupMeetingTitle);

        List<User> members = group.getUserList();
        List<Users> users = new ArrayList<>();

        for (User user : members) {
            List<Days> days = new ArrayList<>();
            for (String date : dates) {

                GroupMeeting meeting = meetings.stream()
                        .filter(groupMeeting -> groupMeeting.getUser().equals(user))
                        .findFirst().get(); // 사용자와 회의제목이 같은 meeting을 가져온다
                When2meet when2meet = when2MeetRepository.findByDateAndUserAndTypeAndGroupMeeting(date, user, type, meeting).get();

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

    public GroupTableDTO addUserMeet(Long groupId, String groupMeetingTitle, MeetType type, String userName) {
        // 사용자는 (그룹, 회의 ID, TYPE을 확인한 후) when2meet 틀을 만든다 즉, 초기화
        Group group = groupRepository.findById(groupId).get();
        List<GroupMeeting> meetings = groupMeetingRepository.findByGroupMeetingTitle(groupMeetingTitle);
        User user = userRepository.findByUserName(userName).get();
        GroupMeeting meeting = meetings.stream()
                .filter(groupMeeting -> groupMeeting.getUser().equals(user))
                .findFirst().get(); // 사용자와 회의제목이 같은 meeting을 가져온다

        List<When2meet> when2meets = when2MeetRepository.findByUserAndTypeAndGroupMeeting(user, type, meeting);

        List<Days> days = new ArrayList<>();
        for(int i=0; i<when2meets.size(); i++){
            RankTime rankTime = new RankTime(group.getGroupTimes().length(), group.getGroupTimes().length(), when2meets.get(i));
            rankTimeRepository.save(rankTime);

            String date = rankTime.getWhen2meet().getDate();
            String day = when2meets.get(i).getDay();
            String time = rankTime.getTime();
            String rank = rankTime.getRank();
            days.add(new Days(date, day, time, rank));
        }

        return new GroupTableDTO(group.getGroupTimes(), type, days);
    }

    public GroupTableDTO<Days> loadUserMeet(Long groupId, MeetType meetType, String userName) {
        User user = userRepository.findByUserName(userName).get();
        Calendar calendar = user.getCalendar();
        List<Meeting> meetings = calendar.getMeetings();
        
        return null;
    }

    public void updateUserMeet(Long groupId, MeetType meetType, String userNames) {
    }

    public void doneGroupMeet(Long groupId, MeetType meetType, String userNames) {
    }
}
