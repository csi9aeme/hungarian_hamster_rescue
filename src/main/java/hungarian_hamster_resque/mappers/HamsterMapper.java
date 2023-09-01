package hungarian_hamster_resque.mappers;

import hungarian_hamster_resque.dtos.hamster.HamsterDto;
import hungarian_hamster_resque.dtos.hamster.HamsterDtoWithoutAdopter;
import hungarian_hamster_resque.models.Hamster;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HamsterMapper {

    HamsterDto toDto(Hamster hamster);
    List<HamsterDto> toDto(List<Hamster> hamsters);

    List<HamsterDtoWithoutAdopter> toDtoWithoutAdopter(List<Hamster> hamsters);
    HamsterDtoWithoutAdopter toDtoWithoutAdopter(Hamster hamster);

    List<HamsterDto> hamsterConverter(List<HamsterDtoWithoutAdopter> hamsters);
}
