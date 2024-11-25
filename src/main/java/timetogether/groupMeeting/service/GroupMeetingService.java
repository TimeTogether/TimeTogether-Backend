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
  public static void getRandomGroupWhereCandidates() throws IOException, URISyntaxException {
    try {
      kakaoApi.keywordSearch("건국대학교");
      kakaoApi.setRadius(1000);
      kakaoApi.categorySearch();
    } catch (Exception e) {
      log.error("장소 검색 중 오류 발생: {}", e.getMessage());
      throw e;
    }
  }
}
