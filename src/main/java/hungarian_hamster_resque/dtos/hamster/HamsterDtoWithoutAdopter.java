package hungarian_hamster_resque.dtos.hamster;

import hungarian_hamster_resque.dtos.host.HostDtoWithoutHamsters;
import hungarian_hamster_resque.enums.Gender;
import hungarian_hamster_resque.enums.HamsterSpecies;
import hungarian_hamster_resque.enums.HamsterStatus;
import hungarian_hamster_resque.models.Picture;
import hungarian_hamster_resque.models.WeeklyReport;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    private List<Picture> pictures;

    private List<WeeklyReport> weeklyReports;

    public HamsterDtoWithoutAdopter(Long id, String name, HamsterSpecies hamsterSpecies, String color, Gender gender, LocalDate dateOfBirth,
            HamsterStatus hamsterStatus, LocalDate startOfFostering, HostDtoWithoutHamsters host,String description, List<Picture> pictures, List<WeeklyReport> weeklyReports) {
        this.id = id;
        this.name = name;
        this.hamsterSpecies = hamsterSpecies;
        this.color = color;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.hamsterStatus = hamsterStatus;
        this.startOfFostering = startOfFostering;
        this.host = host;
        this.description = description;
    }

}
