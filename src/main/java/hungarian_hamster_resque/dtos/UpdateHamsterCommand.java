package hungarian_hamster_resque.dtos;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateHamsterCommand {

    @Schema(description = "név", example = "Bolyhos")
    private String name;

    @Schema(description = "faj", example = "szíriai aranyhörcsög")
    private String hamsterSpecies;

    @Schema(description = "nem", example = "hím")
    private String gender;

    @Schema(description = "születési dátum", example = "2022-12-01")
    private LocalDate dateOfBirth;

    @Schema(description = "örökbefogadhatósági állapot", example = "örökbefogadott")
    private String hamsterStatus;

    @Schema(description = "az ideiglenes befogadó azonosítója", example = "1")
    private Long hostId;

    @Schema(description = "gondozásbavétel dátuma", example = "1")
    private LocalDate startOfFoster;


    @Schema(description = "az új gazda azonosítója", example = "123")
    private Long adoptiveId;

    @Schema(description = "az örökbefogadás dátuma", example = "2023-02-21")
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
