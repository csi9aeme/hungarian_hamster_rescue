package hungarian_hamster_resque.services;

import hungarian_hamster_resque.dtos.*;
import hungarian_hamster_resque.enums.Gender;
import hungarian_hamster_resque.enums.HamsterSpecies;
import hungarian_hamster_resque.enums.HamsterStatus;
import hungarian_hamster_resque.enums.HostStatus;
import hungarian_hamster_resque.exceptions.HamsterStatusNotAcceptableException;
import hungarian_hamster_resque.exceptions.HamsterWithIdNotExistException;
import hungarian_hamster_resque.exceptions.HamsterWithNameNotExist;
import hungarian_hamster_resque.exceptions.HostCantTakeMoreHamstersException;
import hungarian_hamster_resque.mappers.HamsterMapper;
import hungarian_hamster_resque.models.Adoptive;
import hungarian_hamster_resque.models.Hamster;
import hungarian_hamster_resque.models.Host;
import hungarian_hamster_resque.repositories.AdoptiveRepository;
import hungarian_hamster_resque.repositories.HamsterRepository;
import hungarian_hamster_resque.repositories.HostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class HamsterServiceTest {

    @Mock
    HamsterRepository hamsterRepository;

    @Mock
    HostRepository hostRepository;

    @Mock
    AdoptiveRepository adoptiveRepository;

    @Mock
    HamsterMapper mapper;

    @InjectMocks
    HamsterService service;

    Host host;
    HostDtoWithoutHamsters hostDtoWithoutHamsters;
    HostDtoWithHamsters hostDtoWithHamsters;

    Adoptive adoptive;
    AdoptiveDtoWithoutHamsters adoptiveDtoWithoutHamsters;
    AdoptiveDtoWithHamsters adoptiveDtoWithHamsters;


    @BeforeEach
    void init() {
        host = new Host(1L, "Kiss Klára", "1092 Szeged, Őz utca 9", HostStatus.ACTIVE, 1);
        hostDtoWithoutHamsters = new HostDtoWithoutHamsters(1L, "Kiss Klára", "1092 Szeged, Őz utca 9", 1, HostStatus.ACTIVE);
        hostDtoWithHamsters = new HostDtoWithHamsters(1L, "Kiss Klára", "1092 Szeged, Őz utca 9", 1, HostStatus.ACTIVE, new ArrayList<>());

        adoptive = new Adoptive(1L, "Megyek Elemér", "1180 Budapest Havanna utca 8.");
        adoptiveDtoWithoutHamsters = new AdoptiveDtoWithoutHamsters(1L, "Megyek Elemér", "1180 Budapest Havanna utca 8.");
        adoptiveDtoWithHamsters = new AdoptiveDtoWithHamsters(1L, "Megyek Elemér", "1180 Budapest Havanna utca 8.", new ArrayList<>());

    }

    @Test
    void testCreateHamster() {
        when(hostRepository.findById(any())).thenReturn(Optional.of(host));

        when(mapper.toDtoWithoutAdoptive((Hamster) any()))
                .thenReturn(new HamsterDtoWithoutAdoptive(
                        1L,
                        "Bolyhos",
                        HamsterSpecies.CAMPBELL,
                        Gender.MALE,
                        LocalDate.parse("2022-12-29"),
                        HamsterStatus.ADOPTABLE,
                        LocalDate.parse("2023-01-02"),
                        hostDtoWithoutHamsters,
                        "short desc"));

        HamsterDtoWithoutAdoptive hamster = service.createHamster(
                new CreateHamsterCommand("Bolyhos",
                        "dzsungáriai törpehörcsög",
                        "hím",
                        LocalDate.parse("2022-12-29"),
                        "örökbefogadható",
                        1L,
                        LocalDate.parse("2023-01-02"),
                        "short desc"));

        assertThat(hamster.getId()).isNotNull();
        assertThat(hamster.getHost().getName()).isEqualTo("Kiss Klára");

        System.out.println(hamster.getHost().getName());
        verify(hostRepository).findById(anyLong());
        verify(hamsterRepository).save(any());
    }

    @Test
    void testCreateHamsterInvalidStatus() {
        when(hostRepository.findById(any())).thenReturn(Optional.of(host));
        assertThatThrownBy(() ->
                service.createHamster(new CreateHamsterCommand("Mütyürke",
                                "dzsungáriai törpehörcsög",
                                "nőstény",
                                LocalDate.parse("2022-11-01"),
                                "örökbefog",
                                host.getId(),
                                LocalDate.parse("2023-01-25"),
                        "short desc")))
                .isInstanceOf(HamsterStatusNotAcceptableException.class)
                .hasMessage("A megadott örökbefogadhatósági állapot (örökbefog) nem megfelelő.");
    }

    @Test
    void testHostIsFull() {
        when(hostRepository.findById(any())).thenReturn(Optional.of(
                new Host(2L, "Nagy Béla", "1092 Budapest, Őz utca 9", HostStatus.ACTIVE, 0)));

        HostCantTakeMoreHamstersException e = assertThrows(HostCantTakeMoreHamstersException.class,
                () -> service.createHamster(new CreateHamsterCommand("Bolyhos",
                        "campbell törpehörcsög",
                        "nőstény",
                        LocalDate.parse("2022-12-29"),
                        "örökbefogadható",
                        2L,
                        LocalDate.parse("2023-01-02"),
                        "short desc")));

        assertThat(e.getMessage()).isEqualTo("Az ideiglenes befogadó a megadott ID-val (2) nem tud több hörcsögöt fogadni.");

        verify(hostRepository).findById(anyLong());
        verify(hamsterRepository).findFosteringHamstersByHostId(anyLong());
    }

    @Test
    void testUpdateHamsterAllAttributes() {
        when(hostRepository.findById(any())).thenReturn(Optional.of(host));
        when(hamsterRepository.findById(any()))
                .thenReturn(Optional.of(new Hamster(
                        1L,
                        "Bolyhos",
                        HamsterSpecies.CAMPBELL,
                        Gender.MALE,
                        LocalDate.parse("2022-12-29"),
                        HamsterStatus.ADOPTABLE,
                        host,
                        LocalDate.parse("2023-01-02"))));

        when(mapper.toDtoWithoutAdoptive((Hamster) any()))
                .thenReturn(new HamsterDtoWithoutAdoptive(
                        1L,
                        "Bolyhos",
                        HamsterSpecies.CAMPBELL,
                        Gender.MALE,
                        LocalDate.parse("2022-12-29"),
                        HamsterStatus.UNDER_MEDICAL_TREATMENT,
                        LocalDate.parse("2023-01-02"),
                        hostDtoWithoutHamsters,
                        "short desc"));

        HamsterDtoWithoutAdoptive updated = service.updateHamsterAllAttributes(1L,
                new UpdateHamsterCommand(
                        "Bolyhos",
                        "campbell törpehörcsög",
                        "hím",
                        LocalDate.parse("2022-12-29"),
                        "kezelés alatt áll",
                        1L,
                        LocalDate.parse("2023-01-02")
                ));

        assertThat(updated.getHamsterStatus()).isEqualTo(HamsterStatus.UNDER_MEDICAL_TREATMENT);

    }

    @Test
    void testFindAdoptableHamster() {
        when(hamsterRepository.findById(any()))
                .thenReturn(Optional.of(new Hamster(
                        1L,
                        "Bolyhos",
                        HamsterSpecies.CAMPBELL,
                        Gender.MALE,
                        LocalDate.parse("2022-12-29"),
                        HamsterStatus.ADOPTABLE,
                        host,
                        LocalDate.parse("2023-01-02"))));

        when(mapper.toDtoWithoutAdoptive((Hamster) any()))
                .thenReturn(new HamsterDtoWithoutAdoptive(
                        1L,
                        "Bolyhos",
                        HamsterSpecies.CAMPBELL,
                        Gender.MALE,
                        LocalDate.parse("2022-12-29"),
                        HamsterStatus.UNDER_MEDICAL_TREATMENT,
                        LocalDate.parse("2023-01-02"),
                        hostDtoWithoutHamsters,
                        "short desc"));

        HamsterDtoWithoutAdoptive hamster = service.findAdoptableHamsterById(1L);

        assertThat(hamster.getName()).isEqualTo("Bolyhos");

        verify(hamsterRepository).findById(anyLong());
    }

    @Test
    void testFindHamsterByIdNotFound() {
        assertThatThrownBy(() ->
                service.findAdoptableHamsterById(101L))
                .isInstanceOf(HamsterWithIdNotExistException.class)
                .hasMessage("A keresett ID-val (101) hörcsög nincs az adatbázisban.");
        verify(hamsterRepository).findById(any());
    }

    @Test
    void testGetListOfHamsters() {
        when(mapper.toDto((List<Hamster>) any()))
                .thenReturn(List.of(
                        new HamsterDto(
                                1L,
                                "Bolyhos",
                                HamsterSpecies.CAMPBELL,
                                Gender.MALE,
                                LocalDate.parse("2022-12-29"),
                                HamsterStatus.ADOPTED,
                                hostDtoWithoutHamsters,
                                LocalDate.parse("2023-01-02"),
                                adoptiveDtoWithoutHamsters,
                                LocalDate.parse("2023-01-02"),
                                "short desc"),
                        new HamsterDto(
                                1L,
                                "Füles",
                                HamsterSpecies.CAMPBELL,
                                Gender.MALE,
                                LocalDate.parse("2022-12-29"),
                                HamsterStatus.ADOPTABLE,
                                hostDtoWithoutHamsters,
                                LocalDate.parse("2023-01-02"),
                                null,
                                null,
                                "short desc")));

        List<HamsterDto> hamsters = service.getListOfHamsters(Optional.empty());
        assertThat(hamsters).hasSize(2);

        verify(hamsterRepository).findAll();
    }

    @Test
    void testGetListOfHamstersByName() {
        Hamster ham1 = new Hamster(1L,"Bolyhos", HamsterSpecies.CAMPBELL, Gender.MALE, LocalDate.parse("2022-12-29"),
                HamsterStatus.ADOPTED, LocalDate.parse("2023-01-02"),host,
                adoptive, LocalDate.parse("2023-01-02"), "short desc");
        Hamster ham2 = new Hamster(1L,"Boholyka", HamsterSpecies.DWARF, Gender.MALE, LocalDate.parse("2022-12-29"),
                HamsterStatus.ADOPTED, LocalDate.parse("2023-01-02"),host,
                adoptive, LocalDate.parse("2023-01-02"), "short desc");

        when(hamsterRepository.findHamsterByNameContains(anyString()))
                .thenReturn(List.of(ham1, ham2));
        when(mapper.toDto((List<Hamster>) any()))
                .thenReturn(List.of(
                        new HamsterDto(
                                1L,
                                "Bolyhos",
                                HamsterSpecies.CAMPBELL,
                                Gender.MALE,
                                LocalDate.parse("2022-12-29"),
                                HamsterStatus.ADOPTED,
                                hostDtoWithoutHamsters,
                                LocalDate.parse("2023-01-02"),
                                adoptiveDtoWithoutHamsters,
                                LocalDate.parse("2023-01-02"),"short desc"),
                        new HamsterDto(
                                1L,
                                "Boholyka",
                                HamsterSpecies.CAMPBELL,
                                Gender.MALE,
                                LocalDate.parse("2022-12-29"),
                                HamsterStatus.ADOPTABLE,
                                hostDtoWithoutHamsters,
                                LocalDate.parse("2023-01-02"),
                                null,
                                null,
                                "short desc")));

        List<HamsterDto> hamsters = service.getListOfHamsters(Optional.of("Bo"));
        assertThat(hamsters).hasSize(2);

        verify(hamsterRepository).findHamsterByNameContains(anyString());
    }

    @Test
    void testNamePartNotExist() {
        assertThatThrownBy(() ->
                service.getListOfHamsters(Optional.of("Kacsa")))
                .isInstanceOf(HamsterWithNameNotExist.class)
                .hasMessage("A keresett névrészlettel (Kacsa) nincs hörcsög  az adatbázisban.");
        verify(hamsterRepository).findHamsterByNameContains(anyString());
    }

    @Test
    void testAdoptHamster() {
        when(hamsterRepository.findById(anyLong()))
                .thenReturn(Optional.of(new Hamster(
                        1L,
                        "Bolyhos",
                        HamsterSpecies.CAMPBELL,
                        Gender.MALE,
                        LocalDate.parse("2022-12-29"),
                        HamsterStatus.ADOPTABLE,
                        host,
                        LocalDate.parse("2023-01-02"))));

        when(adoptiveRepository.findAdoptiveByIdWithHamsters(anyLong())).thenReturn(adoptive);

        when(mapper.toDto((Hamster) any()))
                .thenReturn(new HamsterDto(
                        1L,
                        "Bolyhos",
                        HamsterSpecies.CAMPBELL,
                        Gender.MALE,
                        LocalDate.parse("2022-12-29"),
                        HamsterStatus.ADOPTED,
                        hostDtoWithoutHamsters,
                        LocalDate.parse("2023-01-02"),
                        adoptiveDtoWithoutHamsters,
                        LocalDate.parse("2023-01-02"),
                        "short desc"));

        HamsterDto hamster = service.adoptHamster(1L,
                new AdoptHamsterCommand(
                        adoptiveDtoWithoutHamsters.getId(),
                        LocalDate.parse("2023-01-02")));

        assertThat(hamster.getAdoptive()).isNotNull();
        assertThat(hamster.getHamsterStatus()).isEqualTo(HamsterStatus.ADOPTED);

        verify(hamsterRepository).findById(anyLong());
        verify(adoptiveRepository).findAdoptiveByIdWithHamsters(anyLong());
        verify(hamsterRepository).save(any());
    }

    @Test
    void testGetListActualFosteringHamsters() {

    }

}