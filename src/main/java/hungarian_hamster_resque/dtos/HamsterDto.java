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

    private String color;

    private Gender gender;

    private LocalDate dateOfBirth;

    private HamsterStatus hamsterStatus;

    private HostDtoWithoutHamsters host;

    private LocalDate startOfFostering;

    private AdopterDtoWithoutHamsters adopter;

    private LocalDate dateOfAdoption;

    private String description;



    public HamsterDto(Long id, String name, HamsterSpecies hamsterSpecies, Gender gender, LocalDate dateOfBirth, HostDtoWithoutHamsters host, LocalDate startOfFostering, LocalDate dateOfAdoption) {
        this.id = id;
        this.name = name;
        this.hamsterSpecies = hamsterSpecies;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.host = host;
        this.startOfFostering = startOfFostering;
        this.dateOfAdoption = dateOfAdoption;
    }


}
