package timetogether.when2meet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import timetogether.groupMeeting.MeetType;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Result {
    private Long id;
    private LocalDateTime meetDTstart;
    private LocalDateTime meetDTend;
    private MeetType meetType;
    private String meetTitle;
    private String groupName;
    private String locationName;
    private String locationUrl;
}
