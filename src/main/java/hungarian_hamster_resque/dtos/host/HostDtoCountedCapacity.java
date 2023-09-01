package hungarian_hamster_resque.dtos.host;

import hungarian_hamster_resque.dtos.AddressDto;
import hungarian_hamster_resque.enums.HostStatus;
import hungarian_hamster_resque.models.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class HostDtoCountedCapacity {

    private long id;

    private String name;

    private AddressDto addressDto;

    private int capacity;

    private int freeCapacity;

    private HostStatus hostStatus;


    public HostDtoCountedCapacity(String name, AddressDto addressDto, int capacity, int freeCapacity, HostStatus hostStatus) {
        this.name = name;
        this.addressDto = addressDto;
        this.capacity = capacity;
        this.freeCapacity = freeCapacity;
        this.hostStatus = hostStatus;
    }
}
