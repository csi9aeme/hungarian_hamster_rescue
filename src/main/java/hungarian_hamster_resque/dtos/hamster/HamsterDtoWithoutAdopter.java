package hungarian_hamster_resque.dtos.hamster;

import hungarian_hamster_resque.dtos.host.HostDtoWithoutHamsters;
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
public class HamsterDtoWithoutAdopter {

    private Long id;

    private String name;

    private HamsterSpecies hamsterSpecies;

    private String color;

    private Gender gender;

    private LocalDate dateOfBirth;

    private HamsterStatus hamsterStatus;

    private LocalDate startOfFostering;

   private HostDtoWithoutHamsters host;

   private String location;

    private String description;

    public HamsterDtoWithoutAdopter(Long id, String name, HamsterSpecies hamsterSpecies, Gender gender, LocalDate dateOfBirth, HamsterStatus hamsterStatus, LocalDate startOfFostering, HostDtoWithoutHamsters host, String description) {
        this.id = id;
        this.name = name;
        this.hamsterSpecies = hamsterSpecies;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.hamsterStatus = hamsterStatus;
        this.startOfFostering = startOfFostering;
        this.host = host;
        this.description = description;
    }




}
