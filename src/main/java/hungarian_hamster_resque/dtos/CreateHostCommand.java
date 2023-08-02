package hungarian_hamster_resque.dtos;

import hungarian_hamster_resque.enums.HamsterStatus;
import hungarian_hamster_resque.enums.HostStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateHostCommand {

    @NotEmpty(message = "Name cannot be empty!")
    @Schema(description = "Name of the temporary host.", example = "Kovács Angéla")
    private String name;

    @NotEmpty(message = "Address cannot be empty!")
    @Schema(description = "Address f the temporary host.", example = "1023 Budapest, Fő utca 28.")
    private String address;

    @Positive(message = "Must be higher than 0.")
    @Schema(description = "Number of hamsters that can be place.", example = "2")
    private int capacity;

    @Schema(description = "The temporary host can accept hamster.", example = "active/inactive")
    private String hostStatus;

    @Schema(description = "List of hamsters in care (all time).")
    private List<HamsterDto> hamsters;


    public CreateHostCommand(String name, String address, int capacity, String hostStatus) {
        this.name = name;
        this.address = address;
        this.capacity = capacity;
        this.hostStatus = hostStatus;
    }


    public CreateHostCommand(String name, String address, int capacity) {
        this.name = name;
        this.address = address;
        this.capacity = capacity;
    }
}
