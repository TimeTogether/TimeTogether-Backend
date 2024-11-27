package timetogether.groupMeeting.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import timetogether.config.kakaoapi.KakaoAPI;
import timetogether.groupMeeting.dto.GroupMeetingCreateRequest;
import timetogether.groupMeeting.dto.GroupMeetingCreateResponse;
import timetogether.groupMeeting.repository.GroupMeetingRepository;

import java.io.IOException;
import java.net.URISyntaxException;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroupMeetingService {
  private static final KakaoAPI kakaoApi = new KakaoAPI();
  private final GroupMeetingRepository groupMeetingRepository;

  public GroupMeetingCreateResponse createMeeting(String socialId, GroupMeetingCreateRequest request) {
    return null;

  }
}
