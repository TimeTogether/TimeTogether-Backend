package timetogether.when2meet.dto;


import lombok.AllArgsConstructor;
import timetogether.groupMeeting.MeetType;

import java.util.List;

@AllArgsConstructor
public class GroupTableDTO<T> {
    private final String groupTimes;
    private final MeetType type;
    private final List<T> users;
}
