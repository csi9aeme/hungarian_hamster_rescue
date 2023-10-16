package hungarian_hamster_rescue.repositories;

import hungarian_hamster_rescue.dtos.ContactsDto;
import hungarian_hamster_rescue.models.Contacts;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContactsMapper {

    ContactsDto toContactsDto(Contacts contacts);


}
