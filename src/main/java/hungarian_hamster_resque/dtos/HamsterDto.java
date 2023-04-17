package hungarian_hamster_resque.dtos;

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

public class HamsterDto {

    private Long id;

    private String name;

    private HamsterSpecies hamsterSpecies;

    private Gender gender;

    private LocalDate dateOfBirth;

    private HamsterStatus hamsterStatus;

    private HostDtoWithoutHamsters host;

    private LocalDate startOfFostering;

    private AdoptiveDtoWithoutHamsters adoptive;

    private LocalDate dateOfAdoption;

}