package hungarian_hamster_resque.dtos.adopter;

import hungarian_hamster_resque.dtos.hamster.HamsterDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdopterDtoWithHamsters {


    private long id;

    private String name;

    private String address;

    private List<HamsterDto> hamsters;

    public AdopterDtoWithHamsters(String name, String address, List<HamsterDto> hamsters) {
        this.name = name;
        this.address = address;
        this.hamsters = hamsters;
    }
}
