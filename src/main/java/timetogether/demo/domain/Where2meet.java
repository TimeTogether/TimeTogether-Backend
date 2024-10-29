package timetogether.demo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Where2meet {

  @Column(name = "location_id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String locationName;

  @Column(nullable = false)
  private String locationUrl;

  public Where2meet(String locationName, String locationUrl) {
    this.locationName = locationName;
    this.locationUrl = locationUrl;
  }
}
