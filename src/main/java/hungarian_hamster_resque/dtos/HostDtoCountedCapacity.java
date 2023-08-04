package hungarian_hamster_resque.dtos;

import hungarian_hamster_resque.enums.HostStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class HostDtoCountedCapacity {

    private Long id;

    private String name;

    private String address;

    private int capacity;

    private int freeCapacity;

    private HostStatus hostStatus;

    public HostDtoCountedCapacity(String name, String address, int capacity, int freeCapacity, HostStatus hostStatus) {
        this.name = name;
        this.address = address;
        this.capacity = capacity;
        this.hostStatus = hostStatus;
        this.freeCapacity = freeCapacity;
    }
}
