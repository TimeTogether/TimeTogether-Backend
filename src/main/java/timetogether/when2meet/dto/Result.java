package timetogether.when2meet.dto;

import jakarta.annotation.Nullable;
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

    public Result(Long id, LocalDateTime meetDTstart, LocalDateTime meetDTend, MeetType meetType, String meetTitle, String groupName) {
        this.id = id;
        this.meetDTstart = meetDTstart;
        this.meetDTend = meetDTend;
        this.meetType = meetType;
        this.meetTitle = meetTitle;
        this.groupName = groupName;
    }
}
