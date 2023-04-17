package hungarian_hamster_resque.dtos;

import hungarian_hamster_resque.enums.HostStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HostDtoWithHamsters {

    private Long id;

    private String name;

    private String address;

    private int capacity;

    private HostStatus hostStatus;

    private List<HamsterDtoSimple> hamsters = new ArrayList<>();
}
