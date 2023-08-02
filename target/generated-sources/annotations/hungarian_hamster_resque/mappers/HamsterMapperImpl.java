package hungarian_hamster_resque.mappers;

import hungarian_hamster_resque.dtos.AdopterDtoWithoutHamsters;
import hungarian_hamster_resque.dtos.HamsterDto;
import hungarian_hamster_resque.dtos.HamsterDtoWithoutAdopter;
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
    date = "2023-08-02T20:27:40+0200",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 20.0.2 (Oracle Corporation)"
)
@Component
public class HamsterMapperImpl implements HamsterMapper {

    @Override
    public HamsterDto toDto(Hamster hamster) {
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
        hamsterDto.setAdoptive( adoptiveToAdopterDtoWithoutHamsters( hamster.getAdoptive() ) );
        hamsterDto.setDateOfAdoption( hamster.getDateOfAdoption() );
        hamsterDto.setDescription( hamster.getDescription() );

        return hamsterDto;
    }

    @Override
    public List<HamsterDto> toDto(List<Hamster> hamsters) {
        if ( hamsters == null ) {
            return null;
        }

        List<HamsterDto> list = new ArrayList<HamsterDto>( hamsters.size() );
        for ( Hamster hamster : hamsters ) {
            list.add( toDto( hamster ) );
        }

        return list;
    }

    @Override
    public List<HamsterDtoWithoutAdopter> toDtoWithoutAdoptive(List<Hamster> hamsters) {
        if ( hamsters == null ) {
            return null;
        }

        List<HamsterDtoWithoutAdopter> list = new ArrayList<HamsterDtoWithoutAdopter>( hamsters.size() );
        for ( Hamster hamster : hamsters ) {
            list.add( toDtoWithoutAdoptive( hamster ) );
        }

        return list;
    }

    @Override
    public HamsterDtoWithoutAdopter toDtoWithoutAdoptive(Hamster hamster) {
        if ( hamster == null ) {
            return null;
        }

        HamsterDtoWithoutAdopter hamsterDtoWithoutAdopter = new HamsterDtoWithoutAdopter();

        hamsterDtoWithoutAdopter.setId( hamster.getId() );
        hamsterDtoWithoutAdopter.setName( hamster.getName() );
        hamsterDtoWithoutAdopter.setHamsterSpecies( hamster.getHamsterSpecies() );
        hamsterDtoWithoutAdopter.setGender( hamster.getGender() );
        hamsterDtoWithoutAdopter.setDateOfBirth( hamster.getDateOfBirth() );
        hamsterDtoWithoutAdopter.setHamsterStatus( hamster.getHamsterStatus() );
        hamsterDtoWithoutAdopter.setStartOfFostering( hamster.getStartOfFostering() );
        hamsterDtoWithoutAdopter.setHost( hostToHostDtoWithoutHamsters( hamster.getHost() ) );
        hamsterDtoWithoutAdopter.setDescription( hamster.getDescription() );

        return hamsterDtoWithoutAdopter;
    }

    @Override
    public List<HamsterDto> hamsterConverter(List<HamsterDtoWithoutAdopter> hamsters) {
        if ( hamsters == null ) {
            return null;
        }

        List<HamsterDto> list = new ArrayList<HamsterDto>( hamsters.size() );
        for ( HamsterDtoWithoutAdopter hamsterDtoWithoutAdopter : hamsters ) {
            list.add( hamsterDtoWithoutAdopterToHamsterDto( hamsterDtoWithoutAdopter ) );
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

    protected AdopterDtoWithoutHamsters adoptiveToAdopterDtoWithoutHamsters(Adoptive adoptive) {
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

    protected HamsterDto hamsterDtoWithoutAdopterToHamsterDto(HamsterDtoWithoutAdopter hamsterDtoWithoutAdopter) {
        if ( hamsterDtoWithoutAdopter == null ) {
            return null;
        }

        HamsterDto hamsterDto = new HamsterDto();

        hamsterDto.setId( hamsterDtoWithoutAdopter.getId() );
        hamsterDto.setName( hamsterDtoWithoutAdopter.getName() );
        hamsterDto.setHamsterSpecies( hamsterDtoWithoutAdopter.getHamsterSpecies() );
        hamsterDto.setGender( hamsterDtoWithoutAdopter.getGender() );
        hamsterDto.setDateOfBirth( hamsterDtoWithoutAdopter.getDateOfBirth() );
        hamsterDto.setHamsterStatus( hamsterDtoWithoutAdopter.getHamsterStatus() );
        hamsterDto.setHost( hamsterDtoWithoutAdopter.getHost() );
        hamsterDto.setStartOfFostering( hamsterDtoWithoutAdopter.getStartOfFostering() );
        hamsterDto.setDescription( hamsterDtoWithoutAdopter.getDescription() );

        return hamsterDto;
    }
}
