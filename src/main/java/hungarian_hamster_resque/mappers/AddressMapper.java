package hungarian_hamster_resque.mappers;

import hungarian_hamster_resque.dtos.AddressDto;
import hungarian_hamster_resque.models.Address;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressDto toAddressDto(Address address);

    List<AddressDto> toAddressDto(List<Address> addresses);
}
