package hungarian_hamster_resque.mappers;

import hungarian_hamster_resque.dtos.AdopterDtoWithHamsters;
import hungarian_hamster_resque.dtos.AdopterDtoWithoutHamsters;
import hungarian_hamster_resque.dtos.HamsterDto;
import hungarian_hamster_resque.dtos.HostDtoWithoutHamsters;
import hungarian_hamster_resque.models.Adopter;
import hungarian_hamster_resque.models.Hamster;
import hungarian_hamster_resque.models.Host;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-02T20:27:41+0200",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 20.0.2 (Oracle Corporation)"
)
@Component
public class AdopterMapperImpl implements AdopterMapper {

    @Override
    public AdopterDtoWithHamsters toDtoWithHamster(Adopter adopter) {
        if ( adopter == null ) {
            return null;
        }

        AdopterDtoWithHamsters adopterDtoWithHamsters = new AdopterDtoWithHamsters();

        if ( adopter.getId() != null ) {
            adopterDtoWithHamsters.setId( adopter.getId() );
        }
        adopterDtoWithHamsters.setName( adopter.getName() );
        adopterDtoWithHamsters.setAddress( adopter.getAddress() );
        adopterDtoWithHamsters.setHamsters( hamsterListToHamsterDtoList( adopter.getHamsters() ) );

        return adopterDtoWithHamsters;
    }

    @Override
    public List<AdopterDtoWithHamsters> toDtoWithHamster(List<Adopter> adopters) {
        if ( adopters == null ) {
            return null;
        }

        List<AdopterDtoWithHamsters> list = new ArrayList<AdopterDtoWithHamsters>( adopters.size() );
        for ( Adopter adopter : adopters) {
            list.add( toDtoWithHamster(adopter) );
        }

        return list;
    }

    @Override
    public AdopterDtoWithoutHamsters toDtoWithoutHamster(Adopter adopter) {
        if ( adopter == null ) {
            return null;
        }

        AdopterDtoWithoutHamsters adopterDtoWithoutHamsters = new AdopterDtoWithoutHamsters();

        if ( adopter.getId() != null ) {
            adopterDtoWithoutHamsters.setId( adopter.getId() );
        }
        adopterDtoWithoutHamsters.setName( adopter.getName() );
        adopterDtoWithoutHamsters.setAddress( adopter.getAddress() );

        return adopterDtoWithoutHamsters;
    }

    @Override
    public List<AdopterDtoWithoutHamsters> toDtoWithoutHamster(List<Adopter> adopters) {
        if ( adopters == null ) {
            return null;
        }

        List<AdopterDtoWithoutHamsters> list = new ArrayList<AdopterDtoWithoutHamsters>( adopters.size() );
        for ( Adopter adopter : adopters) {
            list.add( toDtoWithoutHamster(adopter) );
        }

        return list;
    }

    protected HostDtoWithoutHamsters hostToHostDtoWithoutHamsters(Host host) {
        if ( host == null ) {
            return null;
        }

        HostDtoWithoutHamsters hostDtoWithoutHamsters = new HostDtoWithoutHamsters();

        hostDtoWithoutHamsters.setId( host.getId() );
        hostDtoWithoutHamsters.setName( host.getName() );
        hostDtoWithoutHamsters.setAddress( host.getAddress() );
        hostDtoWithoutHamsters.setCapacity( host.getCapacity() );
        hostDtoWithoutHamsters.setHostStatus( host.getHostStatus() );

        return hostDtoWithoutHamsters;
    }

    protected HamsterDto hamsterToHamsterDto(Hamster hamster) {
        if ( hamster == null ) {
            return null;
        }

        HamsterDto hamsterDto = new HamsterDto();

        hamsterDto.setId( hamster.getId() );
        hamsterDto.setName( hamster.getName() );
        hamsterDto.setHamsterSpecies( hamster.getHamsterSpecies() );
        hamsterDto.setGender( hamster.getGender() );
        hamsterDto.setDateOfBirth( hamster.getDateOfBirth() );
        hamsterDto.setHamsterStatus( hamster.getHamsterStatus() );
        hamsterDto.setHost( hostToHostDtoWithoutHamsters( hamster.getHost() ) );
        hamsterDto.setStartOfFostering( hamster.getStartOfFostering() );
        hamsterDto.setAdopter( toDtoWithoutHamster( hamster.getAdopter() ) );
        hamsterDto.setDateOfAdoption( hamster.getDateOfAdoption() );
        hamsterDto.setDescription( hamster.getDescription() );

        return hamsterDto;
    }

    protected List<HamsterDto> hamsterListToHamsterDtoList(List<Hamster> list) {
        if ( list == null ) {
            return null;
        }

        List<HamsterDto> list1 = new ArrayList<HamsterDto>( list.size() );
        for ( Hamster hamster : list ) {
            list1.add( hamsterToHamsterDto( hamster ) );
        }

        return list1;
    }
}
