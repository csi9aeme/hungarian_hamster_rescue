package hungarian_hamster_resque.dtos.adopter;

import hungarian_hamster_resque.dtos.AddressDto;
import hungarian_hamster_resque.dtos.hamster.HamsterDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdopterDtoWithHamsters {


    private long id;

    private String name;

    private AddressDto address;

    private List<HamsterDto> hamsters;

    public AdopterDtoWithHamsters(String name, AddressDto address, List<HamsterDto> hamsters) {
        this.name = name;
        this.address = address;
        this.hamsters = hamsters;
    }
}
