package timetogether.group.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import timetogether.group.Group;
import timetogether.meeting.MeetType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class GroupCreateResponseDto {
  private String groupName;
  private String groupTitle;
  private String groupImg;
  private String groupMgrId;
  private String groupTimes;
  private String date;
  private MeetType meetType;
  private String groupUrl;
  private String groupMembers;


  @Builder
  public GroupCreateResponseDto(String groupName, String groupTitle, String groupImg, String groupMgrId, String groupTimes, String date, MeetType meetType, String groupUrl, String groupMembers) {
    this.groupName = groupName;
    this.groupTitle = groupTitle;
    this.groupImg = groupImg;
    this.groupMgrId = groupMgrId;
    this.groupTimes = groupTimes;
    this.date = date;
    this.meetType = meetType;
    this.groupUrl = groupUrl;
    this.groupMembers = groupMembers;
    //date를 groupTimes에서 가져와서 변환하여 저장하는 부분
    saveDateFromGroupTimes(groupTimes);
  }

  private void saveDateFromGroupTimes(String groupTimes) {
    if (groupTimes == null || groupTimes.isEmpty()) {
      this.date = "";
      return;
    }

    // 날짜 문자열을 쉼표로 분리
    List<String> dates = Arrays.asList(groupTimes.split(","));

    // 각 날짜를 요일로 변환
    this.date = dates.stream()
            .map(dateStr -> {
              try {
                // yyyy:MM:dd 형식의 문자열을 LocalDate로 파싱
                LocalDate date = LocalDate.parse(dateStr.trim(),
                        DateTimeFormatter.ofPattern("yyyy:MM:dd"));

                // 요일을 한글로 변환
                String dayOfWeek = switch (date.getDayOfWeek()) {
                  case MONDAY -> "월";
                  case TUESDAY -> "화";
                  case WEDNESDAY -> "수";
                  case THURSDAY -> "목";
                  case FRIDAY -> "금";
                  case SATURDAY -> "토";
                  case SUNDAY -> "일";
                };
                return dayOfWeek;
              } catch (Exception e) {
                return "";  // 파싱 실패시 빈 문자열 반환
              }
            })
            .filter(day -> !day.isEmpty())  // 빈 문자열 제거
            .collect(Collectors.joining(","));  // 쉼표로 구분하여 합치기
  }

  public static GroupCreateResponseDto from(Group savedGroup) {
    return GroupCreateResponseDto.builder()
            .groupName(savedGroup.getGroupName())
            .groupTitle(savedGroup.getGroupTitle())
            .groupImg(savedGroup.getGroupImg())
            .groupMgrId(savedGroup.getGroupMgrId())
            .groupTimes(savedGroup.getGroupTimes())
            .date(savedGroup.getDate())
            .meetType(savedGroup.getMeetType())
            .groupUrl(savedGroup.getGroupUrl())
            .groupMembers(savedGroup.getGroupMembers())
            .build();
  }
}