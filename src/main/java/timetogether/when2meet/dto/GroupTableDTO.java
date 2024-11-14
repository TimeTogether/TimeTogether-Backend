package timetogether.when2meet.dto;


import timetogether.groupMeeting.MeetType;

import java.util.List;

public class GroupTableDTO<T> {
    private final String groupTimes;
    private final MeetType type;
    private final List<T> users;

    public GroupTableDTO(String groupTimes, MeetType type, List<T> users) {
        this.groupTimes = groupTimes;
        this.type = type;
        this.users = users;
    }
}
