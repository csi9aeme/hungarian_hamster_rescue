package hungarian_hamster_resque.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {

    private String zip;

    private String town;

    private String street;

    private String houseNumber;

    private String other;

}
