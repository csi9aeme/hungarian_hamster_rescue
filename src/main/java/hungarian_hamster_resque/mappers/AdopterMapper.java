package hungarian_hamster_resque.mappers;

import hungarian_hamster_resque.dtos.AdopterDtoWithHamsters;
import hungarian_hamster_resque.dtos.AdopterDtoWithoutHamsters;
import hungarian_hamster_resque.models.Adopter;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdopterMapper {

    AdopterDtoWithHamsters toDtoWithHamster(Adopter adopter);
    List<AdopterDtoWithHamsters> toDtoWithHamster (List<Adopter> adopters);

    AdopterDtoWithoutHamsters toDtoWithoutHamster(Adopter adopter);
    List<AdopterDtoWithoutHamsters> toDtoWithoutHamster (List<Adopter> adopters);
}
