package timetogether.when2meet.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timetogether.GroupWhere.dto.GroupWhereChooseResponse;
import timetogether.GroupWhere.repository.GroupWhereQueryRepository;
import timetogether.calendar.Calendar;
import timetogether.global.response.BaseResponseStatus;
import timetogether.group.Group;
import timetogether.group.repository.GroupRepository;
import timetogether.groupMeeting.GroupMeeting;
import timetogether.groupMeeting.repository.GroupMeetingRepository;
import timetogether.groupMeeting.MeetType;
import timetogether.meeting.Meeting;
import timetogether.meeting.repository.MeetingRepository;
import timetogether.oauth2.entity.User;
import timetogether.oauth2.repository.UserRepository;
import timetogether.ranktime.RankTime;
import timetogether.ranktime.RankTimeRepository;
import timetogether.when2meet.When2meet;
import timetogether.when2meet.dto.*;
import timetogether.when2meet.dto.Process;
import timetogether.when2meet.exception.Where2MeetIsNull;
import timetogether.when2meet.repository.When2MeetRepository;
import timetogether.where2meet.Where2meet;
import timetogether.where2meet.repository.Where2meetRepository;
import timetogether.where2meet.service.Where2meetService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
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

    public Optional<MeetTableDTO> viewMeetResult(Long groupId) {
        List<Result> resultList = new LinkedList<>();
        List<Process> meetingList = new ArrayList<>();
        String groupName = groupRepository.findGroupNameById(groupId).getGroupName();
        // group service에 더 적합

        // 그룹 이름으로 확정된 미팅 정보를 모두 가져온다
        List<Meeting> meeting = meetingRepository.findByGroupName(groupName); // meet service에 더 적합
        // 그룹 이름으로 생성된 그룹 미팅에 대한 정보를 저장한다
        List<GroupMeeting> groupMeetings = groupMeetingRepository.findByGroupId(groupId);

        Set<String> confirmedTitles = new HashSet<>();
        for (Meeting meet : meeting) {
            confirmedTitles.add(meet.getMeetTitle()); // 확정된 제목을 다 넣는다

            if(meet.getWhere2meet() == null) { // ONLINE
                resultList.add(new Result(meet.getMeetId(), meet.getMeetDTstart(),
                        meet.getMeetDTend(), meet.getMeetType(),
                        meet.getMeetTitle(), groupName));
            }else{ // OFLINE
                resultList.add(new Result(meet.getMeetId(), meet.getMeetDTstart(),
                        meet.getMeetDTend(), meet.getMeetType(),
                        meet.getMeetTitle(), groupName,
                        meet.getWhere2meet().getLocationName(), meet.getWhere2meet().getLocationUrl()));
            }
        }

        //TODO: 확정된 프로세스 이름은 반환하지 않도록 처리
        for(GroupMeeting groupMeeting : groupMeetings){
            if(!confirmedTitles.contains(groupMeeting.getGroupMeetingTitle())) {
                meetingList.add(new Process(groupMeeting.getGroupMeetId(), groupMeeting.getGroupMeetingTitle())); // title + id 추가하기
            }
        }

        MeetTableDTO meetTableDTO = new MeetTableDTO(resultList, meetingList);

        return Optional.ofNullable(meetTableDTO);
    }

    @Transactional
    public void addGroupMeet(String groupMeetingTitle, List<String> dates, Long groupId) {
        log.info("hello");
        Group group = groupRepository.findById(groupId).get(); // 그룹 아이디로 그룹을 조회
        List<User> users = group.getGroupUserList(); // 그룹에서 사용자 리스트 조회

        initGroupMeeting(users, group, groupMeetingTitle);// 회의 일정 초기화, 모든 사용자에 대해 회의 일정 추가
        initWhen2Meet(users, group, groupMeetingTitle, dates); // 특정 날짜 초기화, 모든 사용자에 대해 특정 날짜 추가
    }

    private void initWhen2Meet(List<User> users, Group group, String groupMeetingTitile, List<String> dates) {
        for(int i=0; i<users.size(); i++) {
            log.info("groupMeeting 진입");
            GroupMeeting groupMeeting = groupMeetingRepository.findByGroupAndGroupMeetingTitleAndUser(group, groupMeetingTitile, users.get(i));
            log.info("groupMeeting = {}", groupMeeting.getGroupMeetingTitle());
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

    public GroupTableDTO<Users> viewMeet(Long groupId, String groupMeetingTitle, MeetType type) {
        Group group = groupRepository.findById(groupId).get();
        String groupTimes = group.getGroupTimes();
        GroupMeeting groupMeeting = groupMeetingRepository.findByGroupAndGroupMeetingTitle(group, groupMeetingTitle);
        List<When2meet> when2meets = when2MeetRepository.findByGroupAndGroupMeetingAndType(group, groupMeeting, type);
        List<String> dates = when2meets.stream()
                .map(When2meet::getDate)  // date 필드를 가져옴
                .collect(Collectors.toList());


        List<User> members = group.getGroupUserList();
        List<Users> users = new ArrayList<>();

        for (User user : members) {
            List<Days> days = new ArrayList<>();
            for (String date : dates) {
                // 사용자와 회의일정 제목으로 회의 테이블을 가져온다
                GroupMeeting meeting = groupMeetingRepository.findByGroupAndGroupMeetingTitleAndUser(group, groupMeetingTitle, user);
                // 회의 테이블에서 타입을 확인한 후 when2meet(특정날짜에 대한) 테이블을 가져온다
                When2meet when2meet = when2MeetRepository.findByDateAndTypeAndGroupMeeting(date, type, meeting).get();
                // when2meet으로 ranktime 테이블을 가져온다
                RankTime rankTime = rankTimeRepository.findByWhen2meet(when2meet);

                // ranktime이 null일 경우 (아무것도 설정하지 않은 경우)
                if(rankTime == null){
                    LocalTime groupStartTime = LocalTime.parse(group.getGroupTimes().substring(0, 4), DateTimeFormatter.ofPattern("HHmm"));
                    LocalTime groupEndTime = LocalTime.parse(group.getGroupTimes().substring(4, 8), DateTimeFormatter.ofPattern("HHmm"));

                    String day = when2meet.getDay();
                    String time = generateTime(groupStartTime, groupEndTime, null, null);
                    String rank = generateTime(groupStartTime, groupEndTime, null, null);
                    days.add(new Days(date, day, time, rank));

                }else {
                    String day = when2meet.getDay();
                    String time = rankTime.getTime();
                    String rank = rankTime.getRank();
                    days.add(new Days(date, day, time, rank));
                }
            }
            users.add(new Users(user.getUserName(), days));
        }
        return new GroupTableDTO<Users>(groupTimes, type, users);
    }

    @Transactional
    public GroupTableDTO<Days> addUserMeet(Long groupId, String groupMeetingTitle, MeetType type, String socialId) {
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

        return new GroupTableDTO<Days>(group.getGroupTimes(), type, days);
    }

    public GroupTableDTO<Days> loadUserMeet(Long groupId, String groupMeetingTitle, MeetType type, String socialId) {
        Group group = groupRepository.findById(groupId).get();
        User user = userRepository.findById(socialId).get();

        GroupMeeting groupMeeting = groupMeetingRepository.findByGroupAndGroupMeetingTitle(group, groupMeetingTitle);
        List<When2meet> when2meets = when2MeetRepository.findByGroupAndGroupMeetingAndType(group, groupMeeting, type);
        List<String> dates = when2meets.stream()
                .map(When2meet::getDate)  // date 필드를 가져옴
                .collect(Collectors.toList());

        Calendar calendar = user.getCalendar();
        List<Days> days = new ArrayList<>();

        for(String date : dates) {
            List<Meeting> meetingByDate = meetingRepository.findByCalendarAndDate(calendar.getCalendarId(), date);
            String day = when2MeetRepository.findDaysByDate(date).get(0);

            for(Meeting meeting : meetingByDate){
                LocalDateTime meetDTstart = meeting.getMeetDTstart();
                LocalDate startDate = meetDTstart.toLocalDate(); // 날짜 정보
                LocalTime startTime = meetDTstart.toLocalTime(); // 시간 정보

                LocalDateTime meetDTend = meeting.getMeetDTend();
                LocalDate endDate = meetDTend.toLocalDate();
                LocalTime endTime = meetDTend.toLocalTime();

                LocalTime groupStartTime = LocalTime.parse(group.getGroupTimes().substring(0, 4), DateTimeFormatter.ofPattern("HHmm"));
                LocalTime groupEndTime = LocalTime.parse(group.getGroupTimes().substring(4, 8), DateTimeFormatter.ofPattern("HHmm"));

                String time;
                if(startDate == endDate){ // 시간 정보를 그대로 적용 (날짜 단일)
                    time = generateTime(groupStartTime, groupEndTime, startTime, endTime);

                }else{ // start 날짜일때, 그 사이에 껴있을때, end 날짜일때 (날짜 기간)
                    if(startDate.equals(LocalDate.parse(date))){
                        time = generateTime(groupStartTime, groupEndTime, startTime, groupEndTime);
                    }else if(endDate.equals(LocalDate.parse(date))){
                        time = generateTime(groupStartTime, groupEndTime, groupStartTime, endTime);
                        log.info("time = {}", time);
                    }else{
                        time = generateTime(groupStartTime, groupEndTime, groupStartTime, groupEndTime);
                    }
                }
                days.add(new Days(date, day, time, time));
            }
        }
        
        return new GroupTableDTO<Days>(group.getGroupTimes(), type, days);
    }

    private String generateTime(LocalTime groupStartTime, LocalTime groupEndTime, LocalTime startTime, LocalTime endTime) {

        List<LocalTime> slots = new ArrayList<>();
        LocalTime current = groupStartTime;
        while (groupEndTime.isAfter(current)) {
            slots.add(current);
            current = current.plusMinutes(30);
        } // 초기화

        int[] timeArray = new int[slots.size()];

        if(startTime != null && endTime != null) {
            for (int i = 0; i < slots.size(); i++) {
                if ((slots.get(i).isAfter(startTime) || slots.get(i).equals(startTime)) &&
                        (slots.get(i).isBefore(endTime))) {
                    timeArray[i] = 1; // Mark slot as active
                }
            }
        }

        StringBuilder binaryString = new StringBuilder();
        for (int value : timeArray) {
            binaryString.append(value);
        }

        return binaryString.toString();
    }


    @Transactional
    public void updateUserMeet(Long groupId, String groupMeetingTitle, MeetType type, String socialId, List<Days> days) {
        Group group = groupRepository.findById(groupId).get();
        User user = userRepository.findById(socialId).get();

        GroupMeeting meeting = groupMeetingRepository.findByGroupAndGroupMeetingTitleAndUser(group, groupMeetingTitle, user);
        List<When2meet> when2meets = when2MeetRepository.findByTypeAndGroupMeeting(type, meeting);

        for(int i=0; i<days.size(); i++){ // 넘어온 날짜 사이즈로 하기 (기존 : when2meet size)
            // 해당 when2meet날짜와 같은 day정보를 가져와서 적용
            Days day = days.get(i);
            When2meet when2meet = when2meets.stream().filter(date -> date.getDate().equals(day.getDate())).findFirst().get();

            RankTime rankTime = rankTimeRepository.findByWhen2meet(when2meet);
            rankTime.setRank(day.getRank());
            rankTime.setTime(day.getTime());
            rankTime.setWhen2meet(when2meet);
            rankTimeRepository.save(rankTime);
        }
    }

    @Transactional
    public void doneGroupMeet(Long groupId, String groupMeetingTitle, MeetType type, String socialId, String meetDTJson) throws Where2MeetIsNull, JsonProcessingException {
        Group group = groupRepository.findById(groupId).get();
        User user = userRepository.findById(socialId).get();

        // ObjectMapper 생성
        ObjectMapper objectMapper = new ObjectMapper();
        // JSON 문자열을 JsonNode 객체로 변환
        JsonNode jsonNode = objectMapper.readTree(meetDTJson);
        // "meetDT" 값을 추출
        String meetDT = jsonNode.get("meetDT").asText();
        LocalDateTime finalMeet = LocalDateTime.parse(meetDT);
        Meeting meeting;
        if(type == MeetType.ONLINE) {
            meeting = new Meeting(finalMeet, finalMeet, type, groupMeetingTitle, "", group.getGroupName(), user.getCalendar(), null);
        }else{
            // where2meet Service로 가져온다
            GroupMeeting groupMeeting = groupMeetingRepository.findByGroupAndGroupMeetingTitleAndUser(group, groupMeetingTitle, user); // groupMeetId를 가져오기 위함

            GroupWhereChooseResponse chosenOne = groupWhereQueryRepository.findByChosenOne(groupId, groupMeeting.getGroupMeetId());
            //Where2meet에 저장될 형식으로 변환한다.
            if (chosenOne == null || !chosenOne.isGroupWhereChooseThis()) {
                throw new Where2MeetIsNull(BaseResponseStatus.NOT_EXIST_GROUPWHERE);
            }

            Where2meet where2meet = new Where2meet(chosenOne);
            Where2meet savedWhere2Meet = where2meetRepository.save(where2meet); //groupId와 meetingId 가 Where2meet 테이블에 없어서 여기서 저장

            meeting = new Meeting(finalMeet, finalMeet, type, groupMeetingTitle, "", group.getGroupName(), user.getCalendar(),savedWhere2Meet );
        }
        meetingRepository.save(meeting);
    }

}
