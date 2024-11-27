package timetogether.mypage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import timetogether.when2meet.dto.Days;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserNameDTO {
    private final String userName;
    private final String ImgURL;
}