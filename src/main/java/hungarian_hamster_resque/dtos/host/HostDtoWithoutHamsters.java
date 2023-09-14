package hungarian_hamster_resque.dtos.host;


import hungarian_hamster_resque.dtos.AddressDto;
import hungarian_hamster_resque.enums.HostStatus;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HostDtoWithoutHamsters {

    private Long id;

    private String name;

    private AddressDto addressDto;

    private int capacity;

    private HostStatus hostStatus;

    public HostDtoWithoutHamsters(String name, AddressDto addressDto, int capacity, HostStatus hostStatus) {
        this.name = name;
        this.addressDto = addressDto;
        this.capacity = capacity;
        this.hostStatus = hostStatus;
    }
}
