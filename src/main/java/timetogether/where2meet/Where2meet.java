package timetogether.where2meet;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "where2meet")
public class Where2meet {

  @Column(name = "location_id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long locationId;

  @NotNull
  private String locationName;

  @NotNull
  private String locationUrl;

  @Builder
  public Where2meet(String locationName, String locationUrl) {
    this.locationName = locationName;
    this.locationUrl = locationUrl;
  }
}
