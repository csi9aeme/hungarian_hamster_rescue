package hungarian_hamster_resque.mappers;

import hungarian_hamster_resque.dtos.HamsterDto;
import hungarian_hamster_resque.dtos.HamsterDtoWithoutAdoptive;
import hungarian_hamster_resque.models.Hamster;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HamsterMapper {

    HamsterDto toDto(Hamster hamster);
    List<HamsterDto> toDto(List<Hamster> hamsters);

    List<HamsterDtoWithoutAdoptive> toDtoWithoutAdoptive(List<Hamster> hamsters);
    HamsterDtoWithoutAdoptive toDtoWithoutAdoptive(Hamster hamster);

    List<HamsterDto> hamsterConverter(List<HamsterDtoWithoutAdoptive> hamsters);
}
