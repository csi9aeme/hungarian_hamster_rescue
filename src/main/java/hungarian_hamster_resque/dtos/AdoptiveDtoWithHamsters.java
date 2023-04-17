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


    private String name;

    private String address;

    private List<HamsterDto> hamsters;
}
