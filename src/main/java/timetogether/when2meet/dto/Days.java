package timetogether.when2meet.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class Days {
    private final String date;
    private final String day;
    private final String time;
    private final String rank;

    @JsonCreator
    public Days(
            @JsonProperty("date") String date,
            @JsonProperty("day") String day,
            @JsonProperty("time") String time,
            @JsonProperty("rank") String rank
    ) {
        this.date = date;
        this.day = day;
        this.time = time;
        this.rank = rank;
    }
}
