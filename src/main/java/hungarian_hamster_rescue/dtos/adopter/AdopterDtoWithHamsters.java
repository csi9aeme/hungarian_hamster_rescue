package hungarian_hamster_rescue.dtos.adopter;

import hungarian_hamster_rescue.dtos.AddressDto;
import hungarian_hamster_rescue.dtos.ContactsDto;
import hungarian_hamster_rescue.dtos.hamster.HamsterDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdopterDtoWithHamsters {

    private long id;

    private String name;

    private AddressDto addressDto;

    private ContactsDto contactsDto;

    private List<HamsterDto> hamsters;

    public AdopterDtoWithHamsters(String name, AddressDto addressDto, ContactsDto contactsDto, List<HamsterDto> hamsters) {
        this.name = name;
        this.addressDto = addressDto;
        this.contactsDto = contactsDto;
        this.hamsters = hamsters;
    }

}
