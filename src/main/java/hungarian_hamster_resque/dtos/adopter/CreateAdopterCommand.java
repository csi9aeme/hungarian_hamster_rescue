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

    @NotEmpty(message = "Address cannot be empty!")
    @Schema(description = "address", example = "1023 Budapest, Fő utca 28.")
    private String address;
}
