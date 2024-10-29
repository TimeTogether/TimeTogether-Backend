package timetogether.demo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

  @NotNull
  private String locationName;

  @NotNull
  private String locationUrl;

  public Where2meet(String locationName, String locationUrl) {
    this.locationName = locationName;
    this.locationUrl = locationUrl;
  }
}
