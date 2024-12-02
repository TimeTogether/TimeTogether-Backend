package timetogether.when2meet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;


@AllArgsConstructor
@Getter
public class MeetTableDTO {
    private final List<Process> meeting;
    private final List<Result> result;
}
