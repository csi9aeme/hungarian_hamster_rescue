package hungarian_hamster_resque.dtos.host;

import hungarian_hamster_resque.dtos.hamster.HamsterDto;
import hungarian_hamster_resque.models.Contacts;
import hungarian_hamster_resque.models.WeeklyReport;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
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

    @Positive(message = "Must be higher than 0.")
    @Schema(description = "Number of hamsters that can be place.", example = "2")
    @Min(value = 1)
    private int capacity;

    @Schema(description = "The temporary host can accept hamster.", example = "active/inactive")
    private String hostStatus;

    @Schema(description = "List of hamsters in care (all time).")
    private List<HamsterDto> hamsters;

    @Schema(description = "List of the reports about hamsters.")
    private List<WeeklyReport> weeklyReports;

    @Schema(description = "Phone number", example = "+36201111111")
    private String phoneNumber;

    @NotEmpty
    @Schema(description = "Email address", example = "etc@gmail.com")
    private String email;

    @Schema(description = "Other contact info, like Skype, Messenger ID, etc")
    private String otherContactInfo;

    public CreateHostCommand(String name, String zip, String town, String street, String houseNumber, String other , String phoneNumber, String email, String otherContactInfo, int capacity, String hostStatus) {
        this.name = name;
        this.zip = zip;
        this.town = town;
        this.street = street;
        this.houseNumber = houseNumber;
        this.other = other;
        this.capacity = capacity;
        this.hostStatus = hostStatus;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.otherContactInfo = otherContactInfo;
    }

}
