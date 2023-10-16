package hungarian_hamster_rescue.dtos.host;

import hungarian_hamster_rescue.enums.HostStatus;
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


    @Positive(message = "Must higher than 0!")
    @Schema(description = "Number of hamsters that can be place.", example = "2")
    private int capacity;

    @Schema(description = "The temporary host can accept hamster.", example = "active/inactive")
    private HostStatus hostStatus;

    @Schema(description = "Phone number", example = "+36201111111")
    private String phoneNumber;

    @Schema(description = "Email address", example = "etc@gmail.com")
    private String email;

    @Schema(description = "Other contact info, like Skype, Messenger ID, etc")
    private String otherContactInfo;


    public UpdateHostCommand(String name, String zip, String town, String street, String houseNumber, String other, String phoneNumber, String email, String otherContactInfo, int capacity, HostStatus hostStatus ) {
        this.name = name;
        this.zip = zip;
        this.town = town;
        this.street = street;
        this.houseNumber = houseNumber;
        this.other = other;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.otherContactInfo = otherContactInfo;
        this.capacity = capacity;
        this.hostStatus = hostStatus;

    }
}
