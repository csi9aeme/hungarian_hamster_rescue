package hungarian_hamster_rescue.mappers;

import hungarian_hamster_rescue.dtos.adopter.AdopterDtoWithHamsters;
import hungarian_hamster_rescue.dtos.adopter.AdopterDtoWithoutHamsters;
import hungarian_hamster_rescue.models.Adopter;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdopterMapper {

    AdopterDtoWithHamsters toDtoWithHamster(Adopter adopter);
    List<AdopterDtoWithHamsters> toDtoWithHamster (List<Adopter> adopters);

    AdopterDtoWithoutHamsters toDtoWithoutHamster(Adopter adopter);
    List<AdopterDtoWithoutHamsters> toDtoWithoutHamster (List<Adopter> adopters);
}
