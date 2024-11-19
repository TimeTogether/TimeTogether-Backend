package timetogether.when2meet.dto;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import timetogether.groupMeeting.MeetType;

import java.util.List;

@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class GroupTableDTO<T> {
    private final String groupTimes;
    private final MeetType type;
    private final List<T> users;
}
