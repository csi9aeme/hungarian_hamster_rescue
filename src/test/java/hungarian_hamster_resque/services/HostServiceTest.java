package hungarian_hamster_resque.services;

import hungarian_hamster_resque.dtos.*;
import hungarian_hamster_resque.enums.Gender;
import hungarian_hamster_resque.enums.HamsterSpecies;
import hungarian_hamster_resque.enums.HamsterStatus;
import hungarian_hamster_resque.enums.HostStatus;
import hungarian_hamster_resque.exceptions.HostWithCityNotFoundException;
import hungarian_hamster_resque.exceptions.HostWithIdNotExistException;
import hungarian_hamster_resque.exceptions.HostWithNamePartNotExistException;
import hungarian_hamster_resque.mappers.HostMapper;
import hungarian_hamster_resque.models.Hamster;
import hungarian_hamster_resque.models.Host;
import hungarian_hamster_resque.repositories.HostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.ArrayList;
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

    @InjectMocks
    HostService service;

    @Test
    void testCreateHostDtoWithoutHamWithValidData() {
        when(mapper.toDtoWithoutHam((Host) any()))
                .thenReturn(new HostDtoWithoutHamsters(1L, "Kiss Klára", "1092 Budapest, Virág utca 7", 1, HostStatus.ACTIVE));


        HostDtoWithoutHamsters host = service.createHost(
                new CreateHostCommand("Kiss Klára", "1092 Budapest, Virág utca 7", 1));

        assertThat(host.getId()).isNotNull();
        assertThat(host.getName()).isEqualTo("Kiss Klára");

        verify(mapper, times(1)).toDtoWithoutHam((Host) any());
        verify(repository, times(1)).save(any());

    }

    @Test
    void testUpdateHost() {
        when(repository.findById(anyLong())).thenReturn(
                Optional.of(new Host(1L, "Kiss Klára", "1092 Budapest, Virág utca 7", HostStatus.ACTIVE, 1)));

        when(mapper.toDtoWithoutHam((Host) any()))
                .thenReturn(new HostDtoWithoutHamsters(1L, "Kiss Klára", "1092 Szeged, Őz utca 9", 1, HostStatus.ACTIVE));


        HostDtoWithoutHamsters updated = service.updateHost(1L,
                new UpdateHostCommand("Kiss Klára", "1092 Szeged, Őz utca 9", 1, HostStatus.ACTIVE));

        assertThat(updated.getAddress()).contains("Szeged");
        verify(repository).findById(any());
        verify(repository).save(any());

    }

    @Test
    void testUpdateButWrongId() {
        assertThatThrownBy(() ->
                service.updateHost(101L, new UpdateHostCommand("Kiss Klára", "1092 Szeged, Őz utca 9", 1, HostStatus.ACTIVE)))
                .isInstanceOf(HostWithIdNotExistException.class)
                .hasMessage("A keresett ID-val (101) ideiglenes befogadó nincs az adatbázisban.");
        verify(repository).findById(any());

    }

    @Test
    void testGetListOfHosts() {
        List<HostDtoWithoutHamsters> hosts = new ArrayList<>();
        HostDtoWithoutHamsters host1 = new HostDtoWithoutHamsters(1L, "Kiss Klára", "1092 Budapest, Virág utca 7", 1, HostStatus.ACTIVE);
        HostDtoWithoutHamsters host2 = new HostDtoWithoutHamsters(2L, "Nagy Eszter", "1110 Székesfehérvár, Fő utca 18", 2, HostStatus.ACTIVE);
        HostDtoWithoutHamsters host3 = new HostDtoWithoutHamsters(3L, "Megyek Elemér", "7400 Szekszárd, Virág utca 90", 3, HostStatus.ACTIVE);
        hosts.add(host1);
        hosts.add(host2);
        hosts.add(host3);

        when(mapper.toDtoWithoutHam((List<Host>) any()))
                .thenReturn(hosts);

        List<HostDtoWithoutHamsters> result = service.getListOfHosts(Optional.empty());

        assertThat(result)
                .hasSize(3)
                .extracting(HostDtoWithoutHamsters::getName)
                .contains("Kiss Klára");

        verify(repository).findAll();
    }

    @Test
    void testGetListOfHostByName() {
        Host host1 = new Host(1L, "Kiss Klára", "1092 Budapest, Virág utca 7", HostStatus.ACTIVE, 1);
        Host host2 = new Host(2L, "Kiss Eszter", "1110 Székesfehérvár, Fő utca 18", HostStatus.ACTIVE, 2);

        when(repository.findByNameWithoutHamster("Kiss"))
                .thenReturn(List.of(host1, host2));

        when(mapper.toDtoWithoutHam(List.of(host1, host2)))
                .thenReturn(List.of(
                        new HostDtoWithoutHamsters(1L, "Kiss Klára", "1092 Budapest, Virág utca 7", 1, HostStatus.ACTIVE),
                        new HostDtoWithoutHamsters(2L, "Kiss Eszter", "1110 Székesfehérvár, Fő utca 18", 2, HostStatus.ACTIVE)
                ));

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
                .hasMessage("A keresett névrészlettel (Kelemen) ideiglenes befogadó nincs az adatbázisban.");

        verify(repository).findByNameWithoutHamster(anyString());

    }

    @Test
    void testFindHostById() {
        when(repository.findById(any()))
                .thenReturn(Optional.of(new Host(1L, "Kiss Klára", "1092 Szeged, Őz utca 9", HostStatus.ACTIVE, 1)));

        when(mapper.toDtoWithoutHam((Host) any()))
                .thenReturn(new HostDtoWithoutHamsters(1L, "Kiss Klára", "1092 Szeged, Őz utca 9", 1, HostStatus.ACTIVE));

        HostDtoWithoutHamsters host = service.findHostById(1L);

        assertThat(host.getName()).isEqualTo("Kiss Klára");

        verify(repository).findById(any());
    }

    @Test
    void testFindHostByWrongId() {
        assertThatThrownBy(() ->
                service.findHostById(1000L))
                .isInstanceOf(HostWithIdNotExistException.class)
                .hasMessage("A keresett ID-val (1000) ideiglenes befogadó nincs az adatbázisban.");
        verify(repository).findById(any());

    }

    @Test
    void testGetListOfHostsHamsters() {
       when(repository.findByIdWithAllHamster(anyLong()))
                .thenReturn(new Host(1L, "Kiss Klára", "1092 Szeged, Őz utca 9", HostStatus.ACTIVE, 1,
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
                                LocalDate.parse("2023-01-25")))));

        when(mapper.toDtoWithHam((Host) any()))
                .thenReturn(new HostDtoWithHamsters(1L, "Kiss Klára", "1092 Szeged, Őz utca 9", 1, HostStatus.ACTIVE,
                        List.of(
                                new HamsterDtoSimple("Bolyhos",
                                        HamsterSpecies.DWARF,
                                        Gender.FEMALE,
                                        LocalDate.parse("2022-11-01"),
                                        HamsterStatus.ADOPTABLE,
                                        LocalDate.parse("2023-01-25")),
                                new HamsterDtoSimple("Füles",
                                        HamsterSpecies.DWARF,
                                        Gender.FEMALE,
                                        LocalDate.parse("2022-11-01"),
                                        HamsterStatus.ADOPTABLE,
                                        LocalDate.parse("2023-01-25"))
                        )));

        HostDtoWithHamsters host = service.getListOfHostsHamsters(1L);

        assertThat(host.getHamsters())
                .hasSize(2)
                .extracting(HamsterDtoSimple::getName)
                .containsExactly("Bolyhos", "Füles");

        verify(repository).findByIdWithAllHamster(anyLong());

    }

    @Test
    void testSetHostInactive() {
        when(repository.findById(any()))
                .thenReturn(Optional.of(new Host(1L, "Kiss Klára", "1092 Szeged, Őz utca 9", HostStatus.ACTIVE, 1)));

        when(mapper.toDtoWithoutHam((Host) any()))
                .thenReturn(new HostDtoWithoutHamsters(1L, "Kiss Klára", "1092 Szeged, Őz utca 9", 1, HostStatus.INACTIVE));

        HostDtoWithoutHamsters inactive = service.setHostInactive(1L);
        assertThat(inactive.getHostStatus()).isEqualTo(HostStatus.INACTIVE);

        verify(repository, times(2)).findById(anyLong());

    }

    @Test
    void testGetListOfHostsWithHamstersByCity() {
        Host host1 = new Host(1L, "Kiss Klára", "1092 Budapest, Virág utca 7", HostStatus.ACTIVE, 1);
        Host host2 = new Host(2L, "Nagy Eszter", "1110 Budapest, Fő utca 18", HostStatus.ACTIVE, 2);

        when(repository.findByCityWithHamster("Budapest"))
                .thenReturn(List.of(host1, host2));

        when(mapper.toDtoWithHam(List.of(host1, host2)))
                .thenReturn(List.of(
                        new HostDtoWithHamsters(1L, "Kiss Klára", "1092 Budapest, Virág utca 7", 1, HostStatus.ACTIVE,
                                List.of(new HamsterDtoSimple( "Bolyhos",
                                HamsterSpecies.DWARF,
                                Gender.FEMALE,
                                LocalDate.parse("2022-11-01"),
                                HamsterStatus.ADOPTABLE,
                                LocalDate.parse("2023-01-25")))),
                        new HostDtoWithHamsters(2L, "Kiss Eszter", "1110 Székesfehérvár, Fő utca 18", 2, HostStatus.ACTIVE,
                                List.of(new HamsterDtoSimple("Füles",
                                        HamsterSpecies.DWARF,
                                        Gender.FEMALE,
                                        LocalDate.parse("2022-11-01"),
                                        HamsterStatus.ADOPTABLE,
                                        LocalDate.parse("2023-01-25"))))));

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
                .hasMessage("A keresett településen (Budapest) ideiglenes befogadó nem található.");
        verify(repository).findByCityWithHamster(any());

    }

}