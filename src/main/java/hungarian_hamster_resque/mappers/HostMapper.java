package hungarian_hamster_resque.mappers;

import hungarian_hamster_resque.dtos.HostDtoWithHamsters;
import hungarian_hamster_resque.dtos.HostDtoWithoutHamsters;
import hungarian_hamster_resque.models.Host;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HostMapper {

    HostDtoWithoutHamsters toDtoWithoutHam(Host host);

    List<HostDtoWithoutHamsters> toDtoWithoutHam(List<Host> hosts);

    HostDtoWithHamsters toDtoWithHam(Host host);

    List<HostDtoWithHamsters> toDtoWithHam(List<Host> hosts);



}
