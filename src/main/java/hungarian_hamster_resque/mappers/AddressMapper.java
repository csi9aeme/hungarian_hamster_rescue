package hungarian_hamster_resque.mappers;

import hungarian_hamster_resque.dtos.AddressDto;
import hungarian_hamster_resque.models.Address;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressDto toAddressDto(Address address);

}
