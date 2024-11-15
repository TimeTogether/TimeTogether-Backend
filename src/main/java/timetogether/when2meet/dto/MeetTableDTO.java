package timetogether.when2meet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;


@AllArgsConstructor
public class MeetTableDTO {
    private final List<Result> result;
    private final List<String> meeting;
}
