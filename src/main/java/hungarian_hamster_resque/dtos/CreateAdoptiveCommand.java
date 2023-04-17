package hungarian_hamster_resque.dtos;

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
public class CreateAdoptiveCommand {

    @NotEmpty(message = "A név nem lehet üres!")
    @Schema(description = "név", example = "Kovács Angéla")
    private String name;

    @NotEmpty(message = "A cím nem lehet üres!")
    @Schema(description = "cím", example = "1023 Budapest, Fő utca 28.")
    private String address;
}
