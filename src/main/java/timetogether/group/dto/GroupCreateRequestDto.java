package timetogether.group.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timetogether.group.Group;
import timetogether.group.exception.NotValidMembersException;
import timetogether.meeting.MeetType;

import java.util.Arrays;
import java.util.Optional;

@Getter
@NoArgsConstructor
public class GroupCreateRequestDto {
  private String groupName;
  private String groupTitle;
  private String groupImg;
  private String groupMgrId;
  private String groupTimes;
  private MeetType meetType;
  private String groupUrl;
  private String groupMembers;


  @Builder
  public GroupCreateRequestDto(String groupName, String groupTitle, String groupImg, String groupTimes, MeetType meetType, String groupUrl,String groupMembers) {
    this.groupName = groupName;
    this.groupTitle = groupTitle;
    this.groupImg = groupImg;
    this.groupMgrId = null;
    this.groupTimes = groupTimes;
    this.meetType = meetType;
    this.groupUrl = groupUrl;
    this.groupMembers = groupMembers;

    //date는 함수로 해결 (GropCreateResponseDto에서)
  }

  public Group transferToGroup(String socialId) throws NotValidMembersException {
    validateGroupMembers(groupMembers)
            .orElseThrow(() -> new NotValidMembersException());
    return Group.builder()
            .groupName(groupName)
            .groupTitle(groupTitle)
            .groupImg(groupImg)
            .groupMgrId(socialId)
            .groupTimes(groupTimes)
            .meetType(meetType)
            .groupUrl(groupUrl)
            .groupMembers(groupMembers)
            .build();

  }

  private Optional<String> validateGroupMembers(String groupMembers) {
    // null 체크
    if (groupMembers == null) {
      return Optional.empty();
    }

    try {
      // 쉼표로 구분된 멤버 목록 파싱
      String[] members = groupMembers.split(",");

      // 빈 문자열 체크
      if (members.length == 0) {
        return Optional.empty();
      }

      // 각 멤버 ID 유효성 검사
      boolean isValid = Arrays.stream(members)
              .map(String::trim)
              .allMatch(member -> {
                // 여기에 멤버 ID 형식 검증 로직 추가
                // 예: 길이 체크, 특수문자 체크 등
                return member.length() > 0 &&
                        member.matches("^[0-9]+$"); // 숫자로만 구성된 ID 체크
              });

      return isValid ? Optional.of(groupMembers) : Optional.empty();
    } catch (Exception e) {
      return Optional.empty();
    }
  }
}