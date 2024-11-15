package timetogether.when2meet.dto;

import lombok.Getter;

@Getter
public class Days {
    private final String date;
    private final String day;
    private final String time;
    private final String rank;

    public Days(String date, String day, String time, String rank) {
        this.date = date;
        this.day = day;
        this.time = time;
        this.rank = rank;
    }
}
