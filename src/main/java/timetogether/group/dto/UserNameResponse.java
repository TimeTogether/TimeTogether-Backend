package timetogether.group.dto;

import lombok.Getter;

@Getter
public class UserNameResponse {
  private String userName;

  public UserNameResponse(String userName) {
    this.userName = userName;
  }
}
