package hungarian_hamster_resque.dtos;

import hungarian_hamster_resque.enums.HamsterStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
public class AdoptHamsterCommand {


    @Schema(description = "örökbefogadhatósági állapot", example = "örökbefogadható, örökbefogadott")
    private HamsterStatus hamsterStatus;

    @NotNull
    @Positive
    @Schema(description = "az új gazda azonosítója", example = "123")
    private Long adoptiveId;

    @NotNull
    @Schema(description = "az örökbefogadás dátuma", example = "2023-02-21")
    private LocalDate dateOfAdoption;


}