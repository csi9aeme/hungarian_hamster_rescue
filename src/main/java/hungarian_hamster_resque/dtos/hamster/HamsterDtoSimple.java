package hungarian_hamster_resque.dtos.hamster;

import hungarian_hamster_resque.enums.Gender;
import hungarian_hamster_resque.enums.HamsterSpecies;
import hungarian_hamster_resque.enums.HamsterStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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
}