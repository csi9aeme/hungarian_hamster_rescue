package hungarian_hamster_resque.repositories;

import hungarian_hamster_resque.dtos.ContactsDto;
import hungarian_hamster_resque.models.Contacts;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContactsMapper {

    ContactsDto toContactsDto(Contacts contacts);


}
