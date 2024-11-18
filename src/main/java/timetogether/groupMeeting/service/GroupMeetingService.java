package timetogether.groupMeeting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import timetogether.groupMeeting.dto.GroupMeetingCreateRequest;
import timetogether.groupMeeting.dto.GroupMeetingCreateResponse;
import timetogether.groupMeeting.repository.GroupMeetingRepository;

@Service
@RequiredArgsConstructor
public class GroupMeetingService {
  private final GroupMeetingRepository groupMeetingRepository;

  public GroupMeetingCreateResponse createMeeting(String socialId, GroupMeetingCreateRequest request) {
    return null;

  }
}
