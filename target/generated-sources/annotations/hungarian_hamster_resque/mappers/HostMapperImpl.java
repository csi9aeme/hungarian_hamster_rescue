package hungarian_hamster_resque.mappers;

import hungarian_hamster_resque.dtos.HamsterDtoSimple;
import hungarian_hamster_resque.dtos.HostDtoCountedFreeCapacity;
import hungarian_hamster_resque.dtos.HostDtoWithHamsters;
import hungarian_hamster_resque.dtos.HostDtoWithoutHamsters;
import hungarian_hamster_resque.models.Hamster;
import hungarian_hamster_resque.models.Host;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-02T19:25:27+0200",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 20.0.2 (Oracle Corporation)"
)
@Component
public class HostMapperImpl implements HostMapper {

    @Override
    public HostDtoWithoutHamsters toDtoWithoutHam(Host host) {
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

    @Override
    public List<HostDtoWithoutHamsters> toDtoWithoutHam(List<Host> hosts) {
        if ( hosts == null ) {
            return null;
        }

        List<HostDtoWithoutHamsters> list = new ArrayList<HostDtoWithoutHamsters>( hosts.size() );
        for ( Host host : hosts ) {
            list.add( toDtoWithoutHam( host ) );
        }

        return list;
    }

    @Override
    public HostDtoWithHamsters toDtoWithHam(Host host) {
        if ( host == null ) {
            return null;
        }

        HostDtoWithHamsters hostDtoWithHamsters = new HostDtoWithHamsters();

        hostDtoWithHamsters.setId( host.getId() );
        hostDtoWithHamsters.setName( host.getName() );
        hostDtoWithHamsters.setAddress( host.getAddress() );
        hostDtoWithHamsters.setCapacity( host.getCapacity() );
        hostDtoWithHamsters.setHostStatus( host.getHostStatus() );
        hostDtoWithHamsters.setHamsters( hamsterListToHamsterDtoSimpleList( host.getHamsters() ) );

        return hostDtoWithHamsters;
    }

    @Override
    public List<HostDtoWithHamsters> toDtoWithHam(List<Host> hosts) {
        if ( hosts == null ) {
            return null;
        }

        List<HostDtoWithHamsters> list = new ArrayList<HostDtoWithHamsters>( hosts.size() );
        for ( Host host : hosts ) {
            list.add( toDtoWithHam( host ) );
        }

        return list;
    }

    @Override
    public HostDtoCountedFreeCapacity toDtoFreeCapacity(Host host) {
        if ( host == null ) {
            return null;
        }

        HostDtoCountedFreeCapacity hostDtoCountedFreeCapacity = new HostDtoCountedFreeCapacity();

        hostDtoCountedFreeCapacity.setId( host.getId() );
        hostDtoCountedFreeCapacity.setName( host.getName() );
        hostDtoCountedFreeCapacity.setAddress( host.getAddress() );
        hostDtoCountedFreeCapacity.setCapacity( host.getCapacity() );
        hostDtoCountedFreeCapacity.setHostStatus( host.getHostStatus() );

        return hostDtoCountedFreeCapacity;
    }

    @Override
    public List<HostDtoCountedFreeCapacity> toDtoFreeCapacity(List<Host> hosts) {
        if ( hosts == null ) {
            return null;
        }

        List<HostDtoCountedFreeCapacity> list = new ArrayList<HostDtoCountedFreeCapacity>( hosts.size() );
        for ( Host host : hosts ) {
            list.add( toDtoFreeCapacity( host ) );
        }

        return list;
    }

    protected HamsterDtoSimple hamsterToHamsterDtoSimple(Hamster hamster) {
        if ( hamster == null ) {
            return null;
        }

        HamsterDtoSimple hamsterDtoSimple = new HamsterDtoSimple();

        hamsterDtoSimple.setName( hamster.getName() );
        hamsterDtoSimple.setHamsterSpecies( hamster.getHamsterSpecies() );
        hamsterDtoSimple.setGender( hamster.getGender() );
        hamsterDtoSimple.setDateOfBirth( hamster.getDateOfBirth() );
        hamsterDtoSimple.setHamsterStatus( hamster.getHamsterStatus() );
        hamsterDtoSimple.setStartOfFostering( hamster.getStartOfFostering() );
        hamsterDtoSimple.setDescription( hamster.getDescription() );

        return hamsterDtoSimple;
    }

    protected List<HamsterDtoSimple> hamsterListToHamsterDtoSimpleList(List<Hamster> list) {
        if ( list == null ) {
            return null;
        }

        List<HamsterDtoSimple> list1 = new ArrayList<HamsterDtoSimple>( list.size() );
        for ( Hamster hamster : list ) {
            list1.add( hamsterToHamsterDtoSimple( hamster ) );
        }

        return list1;
    }
}
