package hungarian_hamster_resque.mappers;

import hungarian_hamster_resque.dtos.AdopterDtoWithHamsters;
import hungarian_hamster_resque.dtos.AdopterDtoWithoutHamsters;
import hungarian_hamster_resque.dtos.HamsterDto;
import hungarian_hamster_resque.dtos.HostDtoWithoutHamsters;
import hungarian_hamster_resque.models.Adoptive;
import hungarian_hamster_resque.models.Hamster;
import hungarian_hamster_resque.models.Host;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-02T19:25:26+0200",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 20.0.2 (Oracle Corporation)"
)
@Component
public class AdoptiveMapperImpl implements AdoptiveMapper {

    @Override
    public AdopterDtoWithHamsters toDtoWithHamster(Adoptive adoptive) {
        if ( adoptive == null ) {
            return null;
        }

        AdopterDtoWithHamsters adopterDtoWithHamsters = new AdopterDtoWithHamsters();

        if ( adoptive.getId() != null ) {
            adopterDtoWithHamsters.setId( adoptive.getId() );
        }
        adopterDtoWithHamsters.setName( adoptive.getName() );
        adopterDtoWithHamsters.setAddress( adoptive.getAddress() );
        adopterDtoWithHamsters.setHamsters( hamsterListToHamsterDtoList( adoptive.getHamsters() ) );

        return adopterDtoWithHamsters;
    }

    @Override
    public List<AdopterDtoWithHamsters> toDtoWithHamster(List<Adoptive> adoptives) {
        if ( adoptives == null ) {
            return null;
        }

        List<AdopterDtoWithHamsters> list = new ArrayList<AdopterDtoWithHamsters>( adoptives.size() );
        for ( Adoptive adoptive : adoptives ) {
            list.add( toDtoWithHamster( adoptive ) );
        }

        return list;
    }

    @Override
    public AdopterDtoWithoutHamsters toDtoWithoutHamster(Adoptive adoptive) {
        if ( adoptive == null ) {
            return null;
        }

        AdopterDtoWithoutHamsters adopterDtoWithoutHamsters = new AdopterDtoWithoutHamsters();

        if ( adoptive.getId() != null ) {
            adopterDtoWithoutHamsters.setId( adoptive.getId() );
        }
        adopterDtoWithoutHamsters.setName( adoptive.getName() );
        adopterDtoWithoutHamsters.setAddress( adoptive.getAddress() );

        return adopterDtoWithoutHamsters;
    }

    @Override
    public List<AdopterDtoWithoutHamsters> toDtoWithoutHamster(List<Adoptive> adoptives) {
        if ( adoptives == null ) {
            return null;
        }

        List<AdopterDtoWithoutHamsters> list = new ArrayList<AdopterDtoWithoutHamsters>( adoptives.size() );
        for ( Adoptive adoptive : adoptives ) {
            list.add( toDtoWithoutHamster( adoptive ) );
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
        hamsterDto.setAdoptive( toDtoWithoutHamster( hamster.getAdoptive() ) );
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
