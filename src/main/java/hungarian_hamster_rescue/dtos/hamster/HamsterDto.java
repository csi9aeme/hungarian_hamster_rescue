package hungarian_hamster_rescue.dtos.hamster;

import hungarian_hamster_rescue.dtos.adopter.AdopterDtoWithoutHamsters;
import hungarian_hamster_rescue.dtos.host.HostDtoWithoutHamsters;
import hungarian_hamster_rescue.enums.Gender;
import hungarian_hamster_rescue.enums.HamsterSpecies;
import hungarian_hamster_rescue.enums.HamsterStatus;
import hungarian_hamster_rescue.models.Picture;
import hungarian_hamster_rescue.models.WeeklyReport;
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

    private List<Picture> pictures;

    private List<WeeklyReport> reports;

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

    public HamsterDto(String name, HamsterSpecies hamsterSpecies, String color, Gender gender, LocalDate dateOfBirth, HamsterStatus hamsterStatus, HostDtoWithoutHamsters host, LocalDate startOfFostering, String description, List<Picture> pictures, List<WeeklyReport> reports) {
        this.name = name;
        this.hamsterSpecies = hamsterSpecies;
        this.color = color;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.hamsterStatus = hamsterStatus;
        this.host = host;
        this.startOfFostering = startOfFostering;
        this.description = description;
        this.pictures = pictures;
        this.reports = reports;
    }
}
