package hungarian_hamster_resque.services;

import hungarian_hamster_resque.dtos.AddressDto;
import hungarian_hamster_resque.dtos.hamster.HamsterDtoSimple;
import hungarian_hamster_resque.dtos.host.*;
import hungarian_hamster_resque.enums.Gender;
import hungarian_hamster_resque.enums.HamsterSpecies;
import hungarian_hamster_resque.enums.HamsterStatus;
import hungarian_hamster_resque.enums.HostStatus;
import hungarian_hamster_resque.exceptions.HostHasNotHamsterYetException;
import hungarian_hamster_resque.exceptions.HostWithCityNotFoundException;
import hungarian_hamster_resque.exceptions.HostWithIdNotExistException;
import hungarian_hamster_resque.exceptions.HostWithNamePartNotExistException;
import hungarian_hamster_resque.mappers.AddressMapper;
import hungarian_hamster_resque.mappers.HostMapper;
import hungarian_hamster_resque.models.Address;
import hungarian_hamster_resque.models.Hamster;
import hungarian_hamster_resque.models.Host;
import hungarian_hamster_resque.repositories.HostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.map;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HostServiceTest {

    @Mock
    HostRepository repository;

    @Mock
    HostMapper mapper;

    @Mock
    AddressMapper addressMapper;

    @InjectMocks
    HostService service;

    Address address1;
    Address address2 ;
    Address address3;

    AddressDto addressDto1;
    AddressDto addressDto2;
    AddressDto addressDto3;

    HostDtoWithoutHamsters hostDto1;
    HostDtoWithoutHamsters hostDto2;
    HostDtoWithoutHamsters hostDto3;

    @BeforeEach
    void init(){
        addressDto1 = new AddressDto("1092", "Budapest", "Virág utca" ,"7", "2nd floor");
        addressDto2 = new AddressDto("1018", "Budapest", "Kiss Béla utca", "13.","B");
        addressDto3 = new AddressDto("6700", "Szeged", "Ősz utca" ,"7.","");

        address1 = new Address("1092", "Budapest", "Virág utca" ,"7", "2nd floor");
        address2 = new Address("1018", "Budapest", "Kiss Béla utca", "13.","B");
        address3 = new Address("6700", "Szeged", "Ősz utca" ,"7.","");

        hostDto1 = new HostDtoWithoutHamsters(1L, "Kiss Klára", addressDto1, 1, HostStatus.ACTIVE);
        hostDto2 = new HostDtoWithoutHamsters(2L, "Nagy Eszter", addressDto2, 2, HostStatus.ACTIVE);
        hostDto3 = new HostDtoWithoutHamsters(3L, "Megyek Elemér", addressDto3, 3, HostStatus.ACTIVE);

    }

    @Test
    void testCreateHostDtoWithoutHamWithValidData() {

        CreateHostCommand createHost = new CreateHostCommand("Kiss Klára", "6700", "Szeged", "Ősz utca" ,"7.","", 1, "active");

        when(mapper.toDtoWithoutHam((Host) any()))
                .thenReturn(hostDto1);

        HostDtoWithoutHamsters host = service.createHost(createHost);

        assertThat(host.getId()).isNotNull();
        assertThat(host.getName()).isEqualTo("Kiss Klára");

        verify(mapper, times(1)).toDtoWithoutHam((Host) any());
        verify(repository, times(1)).save(any());

    }

    @Test
    void testUpdateHost() {
        Host host = new Host(1L, "Kiss Klára", address1, HostStatus.ACTIVE, 1);

        UpdateHostCommand updateHostCommand = new UpdateHostCommand("Kiss Klára", "6700", "Szeged", "Ősz utca" ,"7.","", 1, HostStatus.ACTIVE);
        when(repository.findById(anyLong())).thenReturn(Optional.of(host));

        when(addressMapper.toAddressDto((Address) any())).thenReturn(new AddressDto("6700", "Szeged", "Ősz utca" ,"7.",""));
        HostDtoWithoutHamsters newHost = new HostDtoWithoutHamsters( "Kiss Klára", new AddressDto("6700", "Szeged", "Ősz utca" ,"7.",""), 1, HostStatus.ACTIVE);
        
        when(service.updateHost(anyLong(), updateHostCommand)).thenReturn(newHost);

        HostDtoWithoutHamsters updated = service.updateHost(1L, updateHostCommand);

        assertThat(updated.getAddressDto().getTown()).contains("Szeged");

        verify(repository, times(2)).findById(any());
        //verify(repository, times(2)).save(any());

    }

    @Test
    void testUpdateButWrongId() {
        assertThatThrownBy(() ->
                service.updateHost(101L, new UpdateHostCommand("Kiss Klára", "6700", "Szeged", "Ősz utca" ,"7.","", 1, HostStatus.ACTIVE)))
                .isInstanceOf(HostWithIdNotExistException.class)
                .hasMessage("The temporary host with the given ID (101) is not exist.");
        verify(repository).findById(any());

    }


    @Test
    void testGetListOfHosts() {
        List<Host> hostEntities = Arrays.asList(
                new Host(1L, "Kiss Klára", address1, HostStatus.ACTIVE, 1),
                new Host(2L, "Nagy Eszter", address2, HostStatus.ACTIVE, 2),
                new Host(3L, "Megyek Elemér", address3, HostStatus.ACTIVE, 3));

        Optional<String> emptyParam = Optional.empty();

        when(repository.findAll()).thenReturn(hostEntities);
        System.out.println(repository.findAll().size());
        List<HostDtoWithoutHamsters> hosts = new ArrayList<>();
        hosts.add(hostDto1);
        hosts.add(hostDto2);
        hosts.add(hostDto3);


        when(mapper.toDtoWithoutHam(hostEntities)).thenReturn(hosts);
        when(service);
        System.out.println(mapper.toDtoWithoutHam(hostEntities).size());

        when(service.getListOfHosts(emptyParam)).thenReturn(hosts);
        System.out.println(hosts.size());

        List<HostDtoWithoutHamsters> result = service.getListOfHosts(emptyParam);

        assertThat(result)
                .hasSize(3)
                .extracting(HostDtoWithoutHamsters::getName)
                .contains("Kiss Klára");

        verify(repository).findAll();
    }

    @Test
    void testGetListOfHostByName() {
        Host host1 = new Host(1L, "Kiss Klára", address1, HostStatus.ACTIVE, 1);
        Host host2 = new Host(2L, "Kiss Eszter", address2, HostStatus.ACTIVE, 2);

        when(repository.findByNameWithoutHamster("Kiss"))
                .thenReturn(List.of(host1, host2));

        when(mapper.toDtoWithoutHam(List.of(host1, host2)))
                .thenReturn(List.of(hostDto1, hostDto2));

        List<HostDtoWithoutHamsters> result = service.getListOfHosts(Optional.of("Kiss"));

        assertThat(result)
                .hasSize(2)
                .extracting(HostDtoWithoutHamsters::getName)
                .contains("Kiss Klára");

        verify(repository).findByNameWithoutHamster(anyString());
    }

    @Test
    void testGetHostListByNotExistingName() {
        assertThatThrownBy(() ->
                service.getListOfHosts(Optional.of("Kelemen")))
                .isInstanceOf(HostWithNamePartNotExistException.class)
                .hasMessage("The temporary host with the given name (Kelemen) is not exit.");

        verify(repository).findByNameWithoutHamster(anyString());

    }

    @Test
    void testFindHostById() {
        Host host = new Host(1L, "Kiss Klára", address1, HostStatus.ACTIVE, 1);

        when(repository.findById(any())).thenReturn(Optional.of(host));
        when(mapper.toDtoWithoutHam((Host) any())).thenReturn(hostDto1);

        HostDtoWithoutHamsters wantedHost = service.findHostById(1L);

        assertThat(wantedHost.getName()).isEqualTo("Kiss Klára");
        verify(repository).findById(any());
    }

    @Test
    void testFindHostByWrongId() {
        assertThatThrownBy(() ->
                service.findHostById(1000L))
                .isInstanceOf(HostWithIdNotExistException.class)
                .hasMessage("The temporary host with the given ID (1000) is not exist.");
        verify(repository).findById(any());

    }

    @Test
    void testGetListOfHostsHamsters() {
        Host host = new Host(1L, "Kiss Klára", address1, HostStatus.ACTIVE, 1,
                List.of(new Hamster( "Bolyhos",
                                HamsterSpecies.DWARF,
                                Gender.FEMALE,
                                LocalDate.parse("2022-11-01"),
                                HamsterStatus.ADOPTABLE,
                                LocalDate.parse("2023-01-25")),
                        new Hamster("Füles",
                                HamsterSpecies.DWARF,
                                Gender.FEMALE,
                                LocalDate.parse("2022-11-01"),
                                HamsterStatus.ADOPTABLE,
                                LocalDate.parse("2023-01-25"))));
       HostDtoWithHamsters hostDto = new HostDtoWithHamsters(1L, "Kiss Klára", addressDto1, 1, HostStatus.ACTIVE,
               List.of(new HamsterDtoSimple("Bolyhos",
                               HamsterSpecies.DWARF,
                               "dawn",
                               Gender.FEMALE,
                               LocalDate.parse("2022-11-01"),
                               HamsterStatus.ADOPTABLE,
                               LocalDate.parse("2023-01-25"),
                               "short desc"),
                       new HamsterDtoSimple("Füles",
                               HamsterSpecies.DWARF,
                               "dawn",
                               Gender.FEMALE,
                               LocalDate.parse("2022-11-01"),
                               HamsterStatus.ADOPTABLE,
                               LocalDate.parse("2023-01-25"),
                               "short desc")));

        when(repository.findByIdWithAllHamster(anyLong())).thenReturn(host);
        when(mapper.toDtoWithHam((Host) any())).thenReturn(hostDto);

        HostDtoWithHamsters wantedHost = service.getListOfHostsHamsters(1L);

        assertThat(wantedHost.getHamsters())
                .hasSize(2)
                .extracting(HamsterDtoSimple::getName)
                .containsExactly("Bolyhos", "Füles");

        verify(repository).findByIdWithAllHamster(anyLong());

    }

    @Test
    void testHostHasNotHamster() {
        when(repository.findByIdWithAllHamster(anyLong()))
                .thenReturn(new Host(1L, "Kiss Klára", address1, HostStatus.ACTIVE, 1));

        assertThatThrownBy(() ->
                service.getListOfHostsHamsters(1L))
                .isInstanceOf(HostHasNotHamsterYetException.class)
                .hasMessage("The temporary host with the requested ID (1) does not currently have a hamster.");

        verify(repository).findByIdWithAllHamster(anyLong());
    }

    @Test
    void testSetHostInactive() {
        when(repository.findById(any()))
                .thenReturn(Optional.of(new Host(1L, "Kiss Klára", address1, HostStatus.ACTIVE, 1)));

        when(mapper.toDtoWithoutHam((Host) any()))
                .thenReturn(new HostDtoWithoutHamsters(1L, "Kiss Klára", addressDto1, 1, HostStatus.INACTIVE));

        HostDtoWithoutHamsters inactive = service.setHostInactive(1L);
        assertThat(inactive.getHostStatus()).isEqualTo(HostStatus.INACTIVE);

        verify(repository, times(2)).findById(anyLong());

    }

    @Test
    void testGetListOfHostsWithHamstersByCity() {
        Host host1 = new Host(1L, "Kiss Klára", address1, HostStatus.ACTIVE, 1);
        Host host2 = new Host(2L, "Nagy Eszter", address2, HostStatus.ACTIVE, 2);
        HostDtoWithHamsters hostDto1 = new HostDtoWithHamsters(1L, "Kiss Klára", addressDto1, 1, HostStatus.ACTIVE,
                List.of(new HamsterDtoSimple( "Bolyhos",
                        HamsterSpecies.DWARF,
                        "dawn",
                        Gender.FEMALE,
                        LocalDate.parse("2022-11-01"),
                        HamsterStatus.ADOPTABLE,
                        LocalDate.parse("2023-01-25"),
                        "short desc")));
        HostDtoWithHamsters hostDto2 = new HostDtoWithHamsters(2L, "Kiss Eszter", addressDto2, 2, HostStatus.ACTIVE,
                List.of(new HamsterDtoSimple("Füles",
                        HamsterSpecies.DWARF,
                        "dawn",
                        Gender.FEMALE,
                        LocalDate.parse("2022-11-01"),
                        HamsterStatus.ADOPTABLE,
                        LocalDate.parse("2023-01-25"),
                        "short desc")));
        when(repository.findByCityWithHamster("Budapest")).thenReturn(List.of(host1, host2));
        when(mapper.toDtoWithHam(List.of(host1, host2))).thenReturn(List.of(hostDto1, hostDto2));


        List<HostDtoWithHamsters> result = service.getListOfHostsWithHamstersByCity("Budapest");

        assertThat(result)
                .hasSize(2)
                .extracting(HostDtoWithHamsters::getName)
                .contains("Kiss Klára");

        verify(repository).findByCityWithHamster(anyString());
    }

    @Test
    void testGetListOfHostsWithHamstersByWrongCity() {
        assertThatThrownBy(() ->
                service.getListOfHostsWithHamstersByCity("Budapest"))
                .isInstanceOf(HostWithCityNotFoundException.class)
                .hasMessage("There are currently no temporary hosts in the specified city: (Budapest).");
        verify(repository).findByCityWithHamster(any());

    }

    @Test
    void testCountFreeCapacity() {
       when(mapper.toDtoFreeCapacity((List<Host>) any()))
                .thenReturn(List.of(
                        new HostDtoCountedCapacity(1L, "Kiss Klára", addressDto1, 4, 2, HostStatus.ACTIVE),
                        new HostDtoCountedCapacity(2L, "Nagy Klára", addressDto2, 6, 4, HostStatus.ACTIVE)
                        ));
        List<HostDtoCountedCapacity> hostFreeCap = service.getListOfHostWithCapacity();

        int free = hostFreeCap.get(0).getFreeCapacity();
        int free2 = hostFreeCap.get(1).getFreeCapacity();
        assertThat(2).isEqualTo(free);
        assertThat(4).isEqualTo(free2);

    }

    @Test
    void testFreeCapacityCounter(){
        Host host1 = new Host(1L, "Kiss Klára", address1, HostStatus.ACTIVE, 2,
                Arrays.asList(new Hamster("Bolyhos", HamsterSpecies.DWARF, Gender.FEMALE,
                        LocalDate.parse("2022-11-01"), HamsterStatus.ADOPTABLE, LocalDate.parse("2023-01-25"))));
        Host host2 = new Host(2L, "Nagy Klára", address2, HostStatus.ACTIVE, 6,
                Arrays.asList(new Hamster("Boci", HamsterSpecies.DWARF, Gender.FEMALE,
                                LocalDate.parse("2022-11-01"), HamsterStatus.ADOPTABLE, LocalDate.parse("2023-01-25")),
                        new Hamster("Tarka", HamsterSpecies.DWARF, Gender.FEMALE,
                                LocalDate.parse("2022-11-01"), HamsterStatus.ADOPTABLE, LocalDate.parse("2023-01-25"))));
        List<Host> hosts = Arrays.asList(host1, host2);

        HostDtoCountedCapacity hostDto1 = new HostDtoCountedCapacity("Kiss Klára", addressDto1, 2, 1, HostStatus.ACTIVE);
        HostDtoCountedCapacity hostDto2 = new HostDtoCountedCapacity("Nagy Klára", addressDto2, 6, 4, HostStatus.ACTIVE);

        List<HostDtoCountedCapacity> expectedHostDtoList = Arrays.asList(hostDto1, hostDto2);

        when(repository.getListOfHostWithFreeCapacity()).thenReturn(hosts);
        when(mapper.toDtoFreeCapacity(hosts)).thenReturn(expectedHostDtoList);
        when(repository.findByIdWithAllHamster(1L)).thenReturn(host1);
        when(repository.findByIdWithAllHamster(2L)).thenReturn(host2);

        List<HostDtoCountedCapacity> actualHostDtoList = service.getListOfHostWithFreeCapacity();

        assertThat(actualHostDtoList).isEqualTo(expectedHostDtoList);

        verify(repository, times(1)).getListOfHostWithFreeCapacity();
        verify(mapper, times(1)).toDtoFreeCapacity(hosts);
        verify(repository, times(1)).findByIdWithAllHamster(1L);
        verify(repository, times(1)).findByIdWithAllHamster(2L);

    }


}