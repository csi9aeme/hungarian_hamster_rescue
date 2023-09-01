package hungarian_hamster_resque.dtos.host;

import hungarian_hamster_resque.dtos.hamster.HamsterDto;
import hungarian_hamster_resque.models.WeeklyReport;
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

    @Positive(message = "Must be higher than 0.")
    @Schema(description = "Number of hamsters that can be place.", example = "2")
    private int capacity;

    @Schema(description = "The temporary host can accept hamster.", example = "active/inactive")
    private String hostStatus;

    @Schema(description = "List of hamsters in care (all time).")
    private List<HamsterDto> hamsters;

    private List<WeeklyReport> weeklyReports;




//    public CreateHostCommand(String name, String address, int capacity, String hostStatus) {
//        this.name = name;
//        this.address = address;
//        this.capacity = capacity;
//        this.hostStatus = hostStatus;
//    }

    public CreateHostCommand(String name, String zip, String town, String street, String houseNumber, String other, int capacity, String hostStatus) {
        this.name = name;
        this.zip = zip;
        this.town = town;
        this.street = street;
        this.houseNumber = houseNumber;
        this.other = other;
        this.capacity = capacity;
        this.hostStatus = hostStatus;
    }

//    public CreateHostCommand(String name, String address, int capacity) {
//        this.name = name;
//        this.address = address;
//        this.capacity = capacity;
//    }

    public CreateHostCommand(String name, String zip, String town, String street, String houseNumber, String other, int capacity) {
        this.name = name;
        this.zip = zip;
        this.town = town;
        this.street = street;
        this.houseNumber = houseNumber;
        this.other = other;
        this.capacity = capacity;
    }

//    public CreateHostCommand(String name, String address, int capacity, String hostStatus, List<WeeklyReport> weeklyReports) {
//        this.name = name;
//        this.address = address;
//        this.capacity = capacity;
//        this.hostStatus = hostStatus;
//        this.weeklyReports = weeklyReports;
//    }

    public CreateHostCommand(String name, String zip, String town, String street, String houseNumber, String other, int capacity, String hostStatus, List<WeeklyReport> weeklyReports) {
        this.name = name;
        this.zip = zip;
        this.town = town;
        this.street = street;
        this.houseNumber = houseNumber;
        this.other = other;
        this.capacity = capacity;
        this.hostStatus = hostStatus;
        this.weeklyReports = weeklyReports;
    }
}
