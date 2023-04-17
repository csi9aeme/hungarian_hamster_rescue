package hungarian_hamster_resque.dtos;


import hungarian_hamster_resque.enums.HostStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HostDtoWithoutHamsters {

    private Long id;

    private String name;

    private String address;

    private int capacity;

    private HostStatus hostStatus;

    public HostDtoWithoutHamsters(String name, String address, int capacity, HostStatus hostStatus) {
        this.name = name;
        this.address = address;
        this.capacity = capacity;
        this.hostStatus = hostStatus;
    }
}
