package hungarian_hamster_resque.dtos;

import hungarian_hamster_resque.enums.Gender;
import hungarian_hamster_resque.enums.HamsterSpecies;
import hungarian_hamster_resque.enums.HamsterStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateHamsterCommand {

    @NotEmpty(message = "A név nem lehet üres!")
    @Schema(description = "Név", example = "Bolyhos")
    private String name;

    @Schema(description = "faj", example = "szíriai aranyhörcsög")
    private HamsterSpecies hamsterSpecies;

    @Schema(description = "nem", example = "hím")
    private Gender gender;

    @Schema(description = "születési dátum", example = "2022-12-01")
    @PastOrPresent
    private LocalDate dateOfBirth;

    @Schema(description = "örökbefogadhatósági állapot", example = "örökbefogadható, örökbefogadott")
    private HamsterStatus hamsterStatus;

    @Schema(description = "az ideiglenes befogadó azonosítója", example = "1")
    private long hostId;

    @Schema(description = "gondozásbavétel dátuma", example = "2022-12-01")
    private LocalDate startOfFoster;

    }
