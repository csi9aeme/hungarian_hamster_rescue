package hungarian_hamster_resque.dtos;

import hungarian_hamster_resque.enums.HostStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateHostCommand {


    @Schema(description = "név", example = "Kovács Angéla")
    private String name;

    @Schema(description = "cím", example = "1023 Budapest, Fő utca 28.")
    private String address;

    @Positive(message = "Nagyobbnak kell lennie, mint 0!")
    @Schema(description = "elhelyezhető hörcsögök száma", example = "2")
    private int capacity;

    @Schema(description = "Az ideiglenes befogadó tud-e hörcsögöt fogadni", example = "aktv/inaktív")
    private HostStatus hostStatus;


    public UpdateHostCommand(String name, String address, int capacity) {
        this.name = name;
        this.address = address;
        this.capacity = capacity;
    }


}
