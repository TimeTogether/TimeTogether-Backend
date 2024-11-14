package timetogether.meeting;

public enum MeetType {
  OFFLINE, ONLINE;

  public static MeetType fromString(String type) {
    if (type == null) {
      throw new IllegalArgumentException("Type cannot be null");
    }
    switch (type.toUpperCase()) {
      case "ONLINE":
        return ONLINE;
      case "OFFLINE":
        return OFFLINE;
      default:
        throw new IllegalArgumentException("Unknown type: " + type);
    }
  }
}
