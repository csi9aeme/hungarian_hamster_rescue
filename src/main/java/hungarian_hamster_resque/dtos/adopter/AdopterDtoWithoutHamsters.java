package hungarian_hamster_resque.dtos.adopter;

import hungarian_hamster_resque.dtos.AddressDto;
import hungarian_hamster_resque.dtos.ContactsDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdopterDtoWithoutHamsters {

    private long id;

    private String name;

    private AddressDto address;

    private ContactsDto contacts;

    public AdopterDtoWithoutHamsters(String name, AddressDto address, ContactsDto contacts) {
        this.name = name;
        this.address = address;
        this.contacts = contacts;
    }
}
