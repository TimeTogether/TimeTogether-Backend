package timetogether.when2meet.exception;

import lombok.AllArgsConstructor;
import timetogether.global.response.BaseException;
import timetogether.global.response.BaseResponse;
import timetogether.global.response.BaseResponseStatus;

@AllArgsConstructor
public class Where2MeetIsNull extends BaseException{
    public Where2MeetIsNull(BaseResponseStatus status) {super(status);}
}
