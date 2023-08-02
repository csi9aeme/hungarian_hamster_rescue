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


    @Schema(description = "Name.", example = "Kovács Angéla")
    private String name;

    @Schema(description = "Address.", example = "1023 Budapest, Fő utca 28.")
    private String address;

    @Positive(message = "Must higher than 0!")
    @Schema(description = "Number of hamsters that can be place.", example = "2")
    private int capacity;

    @Schema(description = "The temporary host can accept hamster.", example = "aktv/inaktív")
    private HostStatus hostStatus;


    public UpdateHostCommand(String name, String address, int capacity) {
        this.name = name;
        this.address = address;
        this.capacity = capacity;
    }


}
