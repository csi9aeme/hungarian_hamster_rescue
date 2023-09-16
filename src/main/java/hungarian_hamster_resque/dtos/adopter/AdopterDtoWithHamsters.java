package hungarian_hamster_resque.dtos.adopter;

import hungarian_hamster_resque.dtos.AddressDto;
import hungarian_hamster_resque.dtos.ContactsDto;
import hungarian_hamster_resque.dtos.hamster.HamsterDto;
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

    public AdopterDtoWithHamsters(String name, AddressDto addressDto, List<HamsterDto> hamsters) {
        this.name = name;
        this.addressDto = addressDto;
        this.hamsters = hamsters;
    }

    public AdopterDtoWithHamsters(long id, String name, AddressDto addressDto, List<HamsterDto> hamsters) {
        this.id = id;
        this.name = name;
        this.addressDto = addressDto;
        this.hamsters = hamsters;
    }
}
