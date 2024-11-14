package timetogether.when2meet.dto;

import java.util.List;

public class Users {
    private final String userName;
    private final List<Days> days;

    public Users(String userName, List<Days> days) {
        this.userName = userName;
        this.days = days;
    }
}
