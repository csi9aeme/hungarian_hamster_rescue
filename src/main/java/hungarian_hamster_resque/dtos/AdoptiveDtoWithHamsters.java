package hungarian_hamster_resque.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdoptiveDtoWithHamsters {


    private long id;

    private String name;

    private String address;

    private List<HamsterDto> hamsters;

    public AdoptiveDtoWithHamsters(String name, String address, List<HamsterDto> hamsters) {
        this.name = name;
        this.address = address;
        this.hamsters = hamsters;
    }
}
