package hungarian_hamster_resque.dtos;

import hungarian_hamster_resque.enums.Gender;
import hungarian_hamster_resque.enums.HamsterSpecies;
import hungarian_hamster_resque.enums.HamsterStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "A név nem lehet üres!")
    @Schema(description = "név", example = "Bolyhos")
    private String name;

    @NotNull
    @Schema(description = "faj", example = "szíriai aranyhörcsög")
    private String hamsterSpecies;

    @NotNull
    @Schema(description = "nem", example = "hím")
    private String gender;

    @NotNull
    @Schema(description = "születési dátum", example = "2022-12-01")
    @PastOrPresent
    private LocalDate dateOfBirth;

    @NotNull
    @Schema(description = "örökbefogadhatósági állapot", example = "örökbefogadható")
    private String hamsterStatus;

    @NotNull
    @Schema(description = "az ideiglenes befogadó azonosítója", example = "1")
    private long hostId;

    @NotNull
    @Schema(description = "date of starting foster", example = "2022-12-01")
    private LocalDate startOfFoster;

    @Schema(description = "short story of the hamster")
    private String description;



}
