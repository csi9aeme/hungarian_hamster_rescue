package hungarian_hamster_resque.mappers;

import hungarian_hamster_resque.dtos.AdoptiveDtoWithHamsters;
import hungarian_hamster_resque.dtos.AdoptiveDtoWithoutHamsters;
import hungarian_hamster_resque.models.Adoptive;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdoptiveMapper {

    AdoptiveDtoWithHamsters toDtoWithHamster(Adoptive adoptive);
    List<AdoptiveDtoWithHamsters> toDtoWithHamster (List<Adoptive> adoptives);

    AdoptiveDtoWithoutHamsters toDtoWithoutHamster(Adoptive adoptive);
    List<AdoptiveDtoWithoutHamsters> toDtoWithoutHamster (List<Adoptive> adoptives);
}
