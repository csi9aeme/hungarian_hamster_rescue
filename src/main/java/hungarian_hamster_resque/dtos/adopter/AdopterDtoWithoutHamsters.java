package hungarian_hamster_resque.dtos.adopter;

import hungarian_hamster_resque.dtos.AddressDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdopterDtoWithoutHamsters {

    private long id;

    private String name;

    private AddressDto address;

    public AdopterDtoWithoutHamsters(String name, AddressDto address) {
        this.name = name;
        this.address = address;
    }
}
