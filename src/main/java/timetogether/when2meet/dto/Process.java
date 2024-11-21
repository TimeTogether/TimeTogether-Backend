package timetogether.when2meet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class Process {
    private final Long meetId;
    private final String meetTitle;
}
