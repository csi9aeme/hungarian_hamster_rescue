package hungarian_hamster_resque.dtos;

import hungarian_hamster_resque.enums.Gender;
import hungarian_hamster_resque.enums.HamsterSpecies;
import hungarian_hamster_resque.enums.HamsterStatus;
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
    private HamsterSpecies hamsterSpecies;

    @Schema(description = "nem", example = "hím")
    private Gender gender;

    @Schema(description = "születési dátum", example = "2022-12-01")
    private LocalDate dateOfBirth;

    @Schema(description = "örökbefogadhatósági állapot", example = "örökbefogadható, örökbefogadott")
    private HamsterStatus hamsterStatus;

    @Schema(description = "az ideiglenes befogadó azonosítója", example = "1")
    private Long hostId;

    @Schema(description = "gondozásbavétel dátuma", example = "1")
    private LocalDate startOfFoster;


    @Schema(description = "az új gazda azonosítója", example = "123")
    private Long adoptiveId;

    @Schema(description = "az örökbefogadás dátuma", example = "2023-02-21")
    private LocalDate dateOfAdoption;

    public UpdateHamsterCommand(String name, HamsterSpecies hamsterSpecies, Gender gender, LocalDate dateOfBirth, HamsterStatus hamsterStatus, Long hostId, LocalDate startOfFoster) {
        this.name = name;
        this.hamsterSpecies = hamsterSpecies;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.hamsterStatus = hamsterStatus;
        this.hostId = hostId;
        this.startOfFoster = startOfFoster;
    }

    public UpdateHamsterCommand(HamsterStatus hamsterStatus, Long adoptiveId, LocalDate dateOfAdoption) {
        this.hamsterStatus = hamsterStatus;
        this.adoptiveId = adoptiveId;
        this.dateOfAdoption = dateOfAdoption;
    }
}
