package hungarian_hamster_resque.dtos.hamster;

import hungarian_hamster_resque.enums.Gender;
import hungarian_hamster_resque.enums.HamsterSpecies;
import hungarian_hamster_resque.enums.HamsterStatus;
import hungarian_hamster_resque.models.Picture;
import hungarian_hamster_resque.models.WeeklyReport;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HamsterDtoSimple {

    private String name;

    private HamsterSpecies hamsterSpecies;

    private String color;

    private Gender gender;

    private LocalDate dateOfBirth;

    private HamsterStatus hamsterStatus;

    private LocalDate startOfFostering;

    private String description;

    private List<Picture> pictures;

    private List<WeeklyReport> reports;
}
