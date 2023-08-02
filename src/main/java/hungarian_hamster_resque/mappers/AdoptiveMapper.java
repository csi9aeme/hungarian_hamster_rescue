package hungarian_hamster_resque.mappers;

import hungarian_hamster_resque.dtos.AdopterDtoWithHamsters;
import hungarian_hamster_resque.dtos.AdopterDtoWithoutHamsters;
import hungarian_hamster_resque.models.Adoptive;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdoptiveMapper {

    AdopterDtoWithHamsters toDtoWithHamster(Adoptive adoptive);
    List<AdopterDtoWithHamsters> toDtoWithHamster (List<Adoptive> adoptives);

    AdopterDtoWithoutHamsters toDtoWithoutHamster(Adoptive adoptive);
    List<AdopterDtoWithoutHamsters> toDtoWithoutHamster (List<Adoptive> adoptives);
}
