package timetogether.meeting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import timetogether.group.Group;
import timetogether.meeting.repository.MeetingRepository;
import timetogether.oauth2.entity.User;
import timetogether.oauth2.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    public void getMeetByGroup(String socialId) {
        User user = userRepository.findBySocialId(socialId).get();
        List<Group> groupList = user.getGroupList();

        // return 형식 정하기
    }
    
}
