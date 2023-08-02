package hungarian_hamster_resque.dtos;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateHamsterCommand {

    @Schema(description = "Name.", example = "Bolyhos")
    private String name;

    @Schema(description = "Species.", example = "golden hamster")
    private String hamsterSpecies;

    @Schema(description = "Gender.", example = "male")
    private String gender;

    @Schema(description = "Date of birth.", example = "2022-12-01")
    private LocalDate dateOfBirth;

    @Schema(description = "Adoption status of the hamster.", example = "adopted")
    private String hamsterStatus;

    @Schema(description = "ID of the temporary host.", example = "1")
    private Long hostId;

    @Schema(description = "Date of admission.", example = "1")
    private LocalDate startOfFoster;


    @Schema(description = "ID of the new owner.", example = "123")
    private Long adoptiveId;

    @Schema(description = "Date of adoption.", example = "2023-02-21")
    private LocalDate dateOfAdoption;



    public UpdateHamsterCommand(String name, String hamsterSpecies, String gender, LocalDate dateOfBirth, String hamsterStatus, Long hostId, LocalDate startOfFoster) {
        this.name = name;
        this.hamsterSpecies = hamsterSpecies;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.hamsterStatus = hamsterStatus;
        this.hostId = hostId;
        this.startOfFoster = startOfFoster;
    }
}
