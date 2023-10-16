package hungarian_hamster_rescue.dtos.host;


import hungarian_hamster_rescue.dtos.AddressDto;
import hungarian_hamster_rescue.dtos.ContactsDto;
import hungarian_hamster_rescue.enums.HostStatus;
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

    private ContactsDto contactsDto;

    private int capacity;

    private HostStatus hostStatus;

    public HostDtoWithoutHamsters(String name, AddressDto addressDto, ContactsDto contactsDto, int capacity, HostStatus hostStatus) {
        this.name = name;
        this.addressDto = addressDto;
        this.contactsDto = contactsDto;
        this.capacity = capacity;
        this.hostStatus = hostStatus;
    }

    public HostDtoWithoutHamsters(Long id, String name, AddressDto addressDto, int capacity, HostStatus hostStatus) {
        this.id = id;
        this.name = name;
        this.addressDto = addressDto;
        this.capacity = capacity;
        this.hostStatus = hostStatus;
    }

    @Override
    public String toString() {
        return "HostDtoWithoutHamsters{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", addressDto=" + addressDto +
                ", contactsDto=" + contactsDto +
                ", capacity=" + capacity +
                ", hostStatus=" + hostStatus +
                '}';
    }
}
