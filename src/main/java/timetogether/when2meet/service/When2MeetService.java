package timetogether.when2meet.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timetogether.GroupWhere.dto.GroupWhereChooseResponse;
import timetogether.GroupWhere.repository.GroupWhereQueryRepository;
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
import timetogether.when2meet.dto.*;
import timetogether.when2meet.repository.When2MeetRepository;
import timetogether.where2meet.Where2meet;
import timetogether.where2meet.repository.Where2meetRepository;
import timetogether.where2meet.service.Where2meetService;

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
    private final Where2meetService where2meetService;
    private final GroupWhereQueryRepository groupWhereQueryRepository;
    private final Where2meetRepository where2meetRepository;

    public MeetTableDTO viewMeetResult(Long groupId) {
        List<Result> resultList = new LinkedList<>();
        List<String> meetingList = new ArrayList<>();
        String groupName = groupRepository.findGroupNameById(groupId).getGroupName();
        // group service에 더 적합

        // 그룹 이름으로 미팅 정보를 모두 가져온다
        List<Meeting> meeting = meetingRepository.findByGroupName(groupName).get(); // meet service에 더 적합

        //
        for (Meeting meet : meeting) {
            meetingList.add(meet.getMeetTitle()); // title
            resultList.add(new Result(meet.getMeetId(), meet.getMeetDTstart(),
                    meet.getMeetDTend(), meet.getMeetType(),
                    meet.getMeetTitle(), groupName,
                    meet.getWhere2meet().getLocationName(), meet.getWhere2meet().getLocationUrl()));
        }

        return new MeetTableDTO(resultList, meetingList);
    }

    private List<String> dates = new ArrayList<>();
    public void addGroupMeet(String groupMeetingTitle, List<String> dates, Long groupId) {
        Group group = groupRepository.findById(groupId).get(); // 그룹 아이디로 그룹을 조회
        List<User> users = group.getGroupUserList(); // 그룹에서 사용자 리스트 조회

        initGroupMeeting(users, group, groupMeetingTitle);// 회의 일정 초기화, 모든 사용자에 대해 회의 일정 추가
        initWhen2Meet(users, group, groupMeetingTitle, dates); // 특정 날짜 초기화, 모든 사용자에 대해 특정 날짜 추가

        this.dates = dates; // 다른 방식 찾기
    }

    private void initWhen2Meet(List<User> users, Group group, String groupMeetingTitile, List<String> dates) {
        for(int i=0; i<users.size(); i++) {
            GroupMeeting groupMeeting = groupMeetingRepository.findByGroupAndGroupMeetingTitleAndUser(group, groupMeetingTitile, users.get(i));
            for(String date : dates){
                String day = LocalDate.parse(date).getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN); // 요일
                /**
                 * 사용자, 회의 일정 제목을 통해 회의 일정 테이블을 가져온다 (따라서 회의 일정 제목을 입력받을 때, 중복을 허용하면 안된다)
                  */

                When2meet offline = new When2meet(date, day, group, groupMeeting, MeetType.OFFLINE);
                When2meet online = new When2meet(date, day, group, groupMeeting, MeetType.ONLINE);

                when2MeetRepository.save(offline);
                when2MeetRepository.save(online);
            }
            // groupMeeting안에 when2meet 필드가 없기 때문에 별도로 저장하지 않아도 된다
        }
    }

    private void initGroupMeeting(List<User> users, Group group, String groupMeetingTitle) {
        List<GroupMeeting> meetings = new ArrayList<>();
        List<Long> meetId = new ArrayList<>();

        for(int i=0; i<users.size(); i++){
            GroupMeeting groupMeeting = new GroupMeeting(groupMeetingTitle, group, users.get(i)); // 사용자는 회의 제목으로 회의 일정을 찾는다
            users.get(i).initGroupMeeting(groupMeeting); // 사용자에게 회의 일정을 만들어준다 (회의 일정도 사용자를 업데이트한다)

            meetings.add(groupMeeting);
            meetId.add(groupMeeting.getGroupMeetId()); // meetId 저장
        }

        userRepository.saveAll(users); // 사용자 설정을 변경했으므로 모두 저장한다
        groupMeetingRepository.saveAll(meetings); // 사용자에 대한 회의 일정을 모두 저장한다
    }

    public GroupTableDTO viewMeet(Long groupId, String groupMeetingTitle, MeetType type) {
        Group group = groupRepository.findById(groupId).get();
        String groupTimes = group.getGroupTimes();

        List<User> members = group.getGroupUserList();
        List<Users> users = new ArrayList<>();

        for (User user : members) {
            List<Days> days = new ArrayList<>();
            for (String date : dates) {
                // 사용자와 회의일정 제목으로 회의 테이블을 가져온다
                GroupMeeting meeting = groupMeetingRepository.findByGroupAndGroupMeetingTitleAndUser(group, groupMeetingTitle, user);
                // 회의 테이블에서 타입을 확인한 후 when2meet(특정날짜에 대한) 테이블을 가져온다
                When2meet when2meet = when2MeetRepository.findByDateAndUserAndTypeAndGroupMeeting(date, user, type, meeting).get();
                // when2meet으로 ranktime 테이블을 가져온다
                RankTime rankTime = rankTimeRepository.findByWhen2meet(when2meet);

                // ranktime이 null일 경우 (아무것도 설정하지 않은 경우)
                if(rankTime == null) break;

                String day = when2meet.getDay();
                String time = rankTime.getTime();
                String rank = rankTime.getRank();
                days.add(new Days(date, day, time, rank));
            }
            users.add(new Users(user.getUserName(), days));
        }

        return new GroupTableDTO(groupTimes, type, users);
    }

    public GroupTableDTO addUserMeet(Long groupId, String groupMeetingTitle, MeetType type, String socialId) {
        // 사용자는 (그룹, 회의 ID, TYPE을 확인한 후) when2meet 틀을 만든다 즉, 초기화
        Group group = groupRepository.findById(groupId).get();
        User user = userRepository.findById(socialId).get();

        GroupMeeting meeting = groupMeetingRepository.findByGroupAndGroupMeetingTitleAndUser(group, groupMeetingTitle, user);
        List<When2meet> when2meets = when2MeetRepository.findByTypeAndGroupMeeting(type, meeting);
        // 여러 날짜가 넘어옵니다

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

    public GroupTableDTO<Days> loadUserMeet(Long groupId, MeetType meetType, String socialId) {
        User user = userRepository.findById(socialId).get();
        Calendar calendar = user.getCalendar();

        meetingRepository.findByCalendarAndDate(calendar, date);

        
        return null;
    }

    
    public void updateUserMeet(Long groupId, String groupMeetingTitle, MeetType type, String socialId, List<Days> days) {
        Group group = groupRepository.findById(groupId).get();
        User user = userRepository.findById(socialId).get();

        GroupMeeting meeting = groupMeetingRepository.findByGroupAndGroupMeetingTitleAndUser(group, groupMeetingTitle, user);
        List<When2meet> when2meets = when2MeetRepository.findByTypeAndGroupMeeting(type, meeting);

        for(int i=0; i<when2meets.size(); i++){
            When2meet when2meet = when2meets.get(i); // 해당 when2meet날짜와 같은 day정보를 가져와서 적용
            Days day = days.stream().filter(date -> date.getDate().equals(when2meet.getDate())).findFirst().get();

            RankTime rankTime = new RankTime(day.getRank(), day.getTime(), when2meets.get(i));
            rankTimeRepository.save(rankTime);
        }
    }

    @Transactional
    public void doneGroupMeet(Long groupId, String groupMeetingTitle, MeetType type, String socialId, String meetDT) {
        Group group = groupRepository.findById(groupId).get();
        User user = userRepository.findById(socialId).get();

        // groupWhere에서 chosseThis = true 인 부분을 가져온다.
        GroupWhereChooseResponse chosenOne = groupWhereQueryRepository.findByChosenOne(groupId, meetingId); //meetingId(그룹 내 회의 아이디) 필요
        //Where2meet에 저장될 형식으로 변환한다.
        Where2meet where2meet = new Where2meet(chosenOne);
        Where2meet savedWhere2Meet = where2meetRepository.save(where2meet); //groupId와 meetingId 가 Where2meet 테이블에 없어서 여기서 저장

        LocalDate finalMeet = LocalDate.parse(meetDT);
        new Meeting(finalMeet, finalMeet, type, groupMeetingTitle, "", group.getGroupName(), user.getCalendar(),savedWhere2Meet);
    }
}
