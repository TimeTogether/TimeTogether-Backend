package timetogether.group.dto;

import lombok.Builder;
import lombok.Getter;
import timetogether.global.response.BaseResponseStatus;
import timetogether.group.Group;
import timetogether.group.exception.GroupTimesLimitSevenDays;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class GroupAddDatesResponseDto {
  private String groupTimes;
  private String date;
  private String groupUrl;

  @Builder
  public GroupAddDatesResponseDto(String groupTimes, String date, String groupUrl) {
    this.groupTimes = groupTimes;
    this.date = date;
    this.groupUrl = groupUrl;
  }

  public GroupAddDatesResponseDto(Group group) throws GroupTimesLimitSevenDays {
    GroupAddDatesResponseDto result = GroupAddDatesResponseDto.builder()
            .groupTimes(group.getGroupTimes())
            .date(changeGroupTimesToDates(group.getGroupTimes()))
            .groupUrl(makeGroupUrl(group))
            .build();

    this.groupTimes = result.getGroupTimes();
    this.date = result.getDate();
    this.groupUrl = result.getGroupUrl();
  }

  private String makeGroupUrl(Group group) {
    return "http://timetogether.com/group/" + group.getId();
  }

  private String changeGroupTimesToDates(String groupTimes) throws GroupTimesLimitSevenDays {
    String[] dates = groupTimes.split(",");

    if (dates.length > 7) {
      throw new GroupTimesLimitSevenDays(BaseResponseStatus.NOT_VALID_GROUPTIMES);
    }

    //오늘 날짜 가져오기
    LocalDate today = LocalDate.now();
    LocalDate sevenDaysLater = today.plusDays(6); // 오늘 포함 7일

    //오늘 날짜 포맷팅
    List<LocalDate> parsedDates = new ArrayList<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd");

    for (String dateStr : dates) {
      try {
        LocalDate date = LocalDate.parse(dateStr.trim(), formatter);

        if (date.isBefore(today) || date.isAfter(sevenDaysLater)) {
          throw new GroupTimesLimitSevenDays(BaseResponseStatus.NOT_VALID_GROUPTIMES);
        }

        parsedDates.add(date);

      } catch (DateTimeParseException e) {
        throw new GroupTimesLimitSevenDays(BaseResponseStatus.NOT_VALID_GROUPTIMES);
      }
    }

    Collections.sort(parsedDates);

    return parsedDates.stream()
            .map(date -> getDayOfWeekInKorean(date))
            .collect(Collectors.joining(", "));
  }

  private String getDayOfWeekInKorean(LocalDate date) {
    // 한글 요일 매핑
    Map<DayOfWeek, String> koreanDayOfWeek = Map.of(
            DayOfWeek.MONDAY, "월",
            DayOfWeek.TUESDAY, "화",
            DayOfWeek.WEDNESDAY, "수",
            DayOfWeek.THURSDAY, "목",
            DayOfWeek.FRIDAY, "금",
            DayOfWeek.SATURDAY, "토",
            DayOfWeek.SUNDAY, "일"
    );

    return koreanDayOfWeek.get(date.getDayOfWeek());
  }
}

