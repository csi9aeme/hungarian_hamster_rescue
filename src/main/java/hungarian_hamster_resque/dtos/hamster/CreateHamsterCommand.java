package hungarian_hamster_resque.dtos.hamster;

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

    @NotEmpty(message = "Name cannot be empty!")
    @NotNull(message = "Name cannot be empty!")
    @Schema(description = "Name of the hamster.", example = "Bolyhos")
    private String name;

    @NotNull
    @Schema(description = "Species of the hamster.", example = "Golden hamster")
    private String hamsterSpecies;

    @NotNull
    @Schema(description = "Color of the hamster", example = "agouti")
    private String color;

    @NotNull
    @Schema(description = "Gender of the hamster.", example = "male")
    private String gender;

    @NotNull
    @Schema(description = "Date of birth.", example = "2022-12-01")
    @PastOrPresent
    private LocalDate dateOfBirth;

    @NotNull
    @Schema(description = "Adoption status of the hamster.", example = "Adoptable")
    private String hamsterStatus;

    @NotNull
    @Schema(description = "ID of the temporary host.", example = "1")
    private long hostId;

    @NotNull
    @Schema(description = "Date of admission.", example = "2022-12-01")
    private LocalDate startOfFoster;

    @Schema(description = "Description of the hamster.")
    private String description;

}
