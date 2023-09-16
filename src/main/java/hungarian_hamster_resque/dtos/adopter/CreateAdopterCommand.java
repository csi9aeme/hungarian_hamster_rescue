package hungarian_hamster_resque.dtos.adopter;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateAdopterCommand {

    @NotEmpty(message = "Name cannot be empty!")
    @Schema(description = "name", example = "Kovács Angéla")
    private String name;

    @NotEmpty(message = "ZIP cannot be empty!")
    @Schema(description = "ZIP code of the address", example = "1023")
    private String zip;

    @NotEmpty(message = "Town cannot be empty!")
    @Schema(description = "Town of the address.", example = "Budapest")
    private String town;

    @NotEmpty(message = "Street cannot be empty!")
    @Schema(description = "Street of the address", example = "Fő utca")
    private String street;

    @NotEmpty(message = "House number cannot be empty!")
    @Schema(description = "Number of the house", example = "Fő utca 28.")
    private String houseNumber;


    @Schema(description = "Other info (level, etc)", example = "1. floor B door")
    private String other;

    private String phoneNumber;

    private String email;

    private String otherContactInfo;

}
