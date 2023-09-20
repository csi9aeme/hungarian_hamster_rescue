package hungarian_hamster_resque.dtos.host;

import hungarian_hamster_resque.dtos.AddressDto;
import hungarian_hamster_resque.dtos.ContactsDto;
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

    private long id;

    private String name;

    private String location;

    private ContactsDto contactsDto;

    private int capacityAll;

    private int freeCapacity;

    private HostStatus hostStatus;

    public HostDtoCountedCapacity(String name, String location, ContactsDto contactsDto, int capacityAll, int freeCapacity, HostStatus hostStatus) {
        this.name = name;
        this.location = location;
        this.contactsDto = contactsDto;
        this.capacityAll = capacityAll;
        this.freeCapacity = freeCapacity;
        this.hostStatus = hostStatus;
    }


}
