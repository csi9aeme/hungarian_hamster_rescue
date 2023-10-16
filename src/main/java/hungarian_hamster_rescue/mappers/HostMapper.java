package hungarian_hamster_rescue.mappers;

import hungarian_hamster_rescue.dtos.host.HostDtoCountedCapacity;
import hungarian_hamster_rescue.dtos.host.HostDtoWithHamsters;
import hungarian_hamster_rescue.dtos.host.HostDtoWithoutHamsters;
import hungarian_hamster_rescue.models.Host;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HostMapper {

    @Mapping(source = "address", target = "addressDto")
    @Mapping(source = "contacts", target = "contactsDto")
    HostDtoWithoutHamsters toDtoWithoutHam(Host host);

    @IterableMapping(elementTargetType = HostDtoWithoutHamsters.class)
    List<HostDtoWithoutHamsters> toDtoWithoutHam(List<Host> hosts);

    @Mapping(source = "address", target = "addressDto")
    @Mapping(source = "contacts", target = "contactsDto")
    HostDtoWithHamsters toDtoWithHam(Host host);

    @IterableMapping(elementTargetType = HostDtoWithHamsters.class)
    List<HostDtoWithHamsters> toDtoWithHam(List<Host> hosts);

    @Mapping(source = "contacts", target = "contactsDto")
    @Mapping(source = "capacity", target = "capacityAll")
    HostDtoCountedCapacity toDtoFreeCapacity(Host host);

    @IterableMapping(elementTargetType = HostDtoCountedCapacity.class)
    List<HostDtoCountedCapacity> toDtoFreeCapacity(List<Host> hosts);



}
