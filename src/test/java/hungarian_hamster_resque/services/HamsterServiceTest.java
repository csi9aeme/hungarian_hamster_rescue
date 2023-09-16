package hungarian_hamster_resque.services;

import hungarian_hamster_resque.dtos.AddressDto;
import hungarian_hamster_resque.dtos.ContactsDto;
import hungarian_hamster_resque.dtos.adopter.AdoptHamsterCommand;
import hungarian_hamster_resque.dtos.adopter.AdopterDtoWithHamsters;
import hungarian_hamster_resque.dtos.adopter.AdopterDtoWithoutHamsters;
import hungarian_hamster_resque.dtos.hamster.CreateHamsterCommand;
import hungarian_hamster_resque.dtos.hamster.HamsterDto;
import hungarian_hamster_resque.dtos.hamster.HamsterDtoWithoutAdopter;
import hungarian_hamster_resque.dtos.hamster.UpdateHamsterCommand;
import hungarian_hamster_resque.dtos.host.HostDtoWithHamsters;
import hungarian_hamster_resque.dtos.host.HostDtoWithoutHamsters;
import hungarian_hamster_resque.enums.Gender;
import hungarian_hamster_resque.enums.HamsterSpecies;
import hungarian_hamster_resque.enums.HamsterStatus;
import hungarian_hamster_resque.enums.HostStatus;
import hungarian_hamster_resque.exceptions.HamsterStatusNotAcceptableException;
import hungarian_hamster_resque.exceptions.HamsterWithIdNotExistException;
import hungarian_hamster_resque.exceptions.HamsterWithNameNotExist;
import hungarian_hamster_resque.exceptions.HostCantTakeMoreHamstersException;
import hungarian_hamster_resque.mappers.HamsterMapper;
import hungarian_hamster_resque.models.*;
import hungarian_hamster_resque.repositories.AdopterRepository;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class HamsterServiceTest {

    @Mock
    HamsterRepository hamsterRepository;

    @Mock
    HostRepository hostRepository;

    @Mock
    AdopterRepository adopterRepository;

    @Mock
    HamsterMapper mapper;

    @InjectMocks
    HamsterService service;

    Host host;
    HostDtoWithoutHamsters hostDtoWithoutHamsters;
    HostDtoWithHamsters hostDtoWithHamsters;

    Adopter adopter;
    AdopterDtoWithoutHamsters adopterDtoWithoutHamsters;
    AdopterDtoWithHamsters adopterDtoWithHamsters;
    AddressDto addressDto1;
    AddressDto addressDto2;
    AddressDto addressDto3;

    Contacts contacts1;
    ContactsDto contactsDto1;

    Hamster hamsterBolyhos;
    HamsterDtoWithoutAdopter hamsterDtoWithoutAdopterBolyhos;
    CreateHamsterCommand createBolyhos;

    @BeforeEach
    void init() {
        addressDto1 = new AddressDto("1092", "Budapest", "Virág utca" ,"7", "2nd floor");
        addressDto2 = new AddressDto("1018", "Budapest", "Kiss Béla utca", "13.","B");
        addressDto3 = new AddressDto("6700", "Szeged", "Ősz utca" ,"7.","");

        contacts1 = new Contacts("+36201112222", "egyik@gmail.com", "skype");
        contactsDto1 = new ContactsDto("+36201112222", "egyik@gmail.com", "skype");

        host = new Host(1L, "Kiss Klára", new Address("6700", "Szeged", "Őz utca", "9","2/15"), HostStatus.ACTIVE, 3);
        hostDtoWithoutHamsters = new HostDtoWithoutHamsters(1L, "Kiss Klára", addressDto1, 1, HostStatus.ACTIVE);
        hostDtoWithHamsters = new HostDtoWithHamsters(1L, "Kiss Klára", addressDto1, 1, HostStatus.ACTIVE, new ArrayList<>());

        adopter = new Adopter(1L, "Megyek Elemér", new Address("1180", "Budapest", "Havanna utca", "8.", "Fsz.7."));
        adopterDtoWithoutHamsters = new AdopterDtoWithoutHamsters(1L, "Megyek Elemér", addressDto2, contactsDto1 );
        adopterDtoWithHamsters = new AdopterDtoWithHamsters("Megyek Elemér", addressDto3, contactsDto1, new ArrayList<>());

        hamsterBolyhos = new Hamster(1L, "Bolyhos", HamsterSpecies.CAMPBELL, "dawn", Gender.MALE,
                LocalDate.parse("2022-12-29"), HamsterStatus.ADOPTABLE, LocalDate.parse("2023-01-02"), host,
                "short desc", new ArrayList<>(), new ArrayList<>());
        hamsterDtoWithoutAdopterBolyhos = new HamsterDtoWithoutAdopter(1L, "Bolyhos", HamsterSpecies.CAMPBELL,
                "dawn", Gender.MALE, LocalDate.parse("2022-12-29"), HamsterStatus.ADOPTABLE, LocalDate.parse("2023-01-02"),
                hostDtoWithoutHamsters, "Szeged", "short desc");
        createBolyhos = new CreateHamsterCommand("Bolyhos", "campbell's dwarf hamster", "dawn",
                "male", LocalDate.parse("2022-12-29"), "adoptable", host.getId(),
                LocalDate.parse("2023-01-02"), "short desc");
    }

    @Test
    void testCreateHamster() {
        when(hostRepository.findById(any())).thenReturn(Optional.of(host));
        when(hamsterRepository.findById(any())).thenReturn(Optional.of(hamsterBolyhos));

        when(mapper.toDtoWithoutAdopter((Hamster) any()))
                .thenReturn(hamsterDtoWithoutAdopterBolyhos);

        HamsterDtoWithoutAdopter hamster = service.createHamster(createBolyhos);

        assertThat(hamster.getId()).isNotNull();
        assertThat(hamster.getHost().getName()).isEqualTo("Kiss Klára");

        verify(hostRepository).findById(anyLong());
        verify(hamsterRepository).save(any());
    }

    @Test
    void testCreateHamsterInvalidStatus() {
        when(hostRepository.findById(any())).thenReturn(Optional.of(host));
        assertThatThrownBy(() ->
                service.createHamster(new CreateHamsterCommand("Mütyürke",
                                "djungarian dwarf hamster",
                                "dawn",
                                "female",
                                LocalDate.parse("2022-11-01"),
                                "adoptab",
                                host.getId(),
                                LocalDate.parse("2023-01-25"),
                        "short desc")))
                .isInstanceOf(HamsterStatusNotAcceptableException.class)
                .hasMessage("The given status (adoptab) is not acceptable.");
    }

    @Test
    void testHostIsFull() {
        when(hostRepository.findById(any())).thenReturn(Optional.of(
                new Host(2L, "Nagy Béla", new Address("1092", "Budapest", "Őz utca", "9", "2.em"), HostStatus.ACTIVE, 0)));

        HostCantTakeMoreHamstersException e = assertThrows(HostCantTakeMoreHamstersException.class,
                () -> service.createHamster(createBolyhos));

        assertThat(e.getMessage()).isEqualTo("The temporary host with the given ID (2) can't take more hamster.");

        verify(hostRepository).findById(anyLong());
        verify(hamsterRepository).findFosteringHamstersByHostId(anyLong());
    }

    @Test
    void testUpdateHamsterAllAttributes() {
        when(hostRepository.findById(any())).thenReturn(Optional.of(host));
        when(hamsterRepository.findById(any()))
                .thenReturn(Optional.of(hamsterBolyhos));

        when(mapper.toDtoWithoutAdopter((Hamster) any()))
                .thenReturn(hamsterDtoWithoutAdopterBolyhos);

        HamsterDtoWithoutAdopter updated = service.updateHamsterAllAttributes(1L,
                new UpdateHamsterCommand(
                        "Bolyhos",
                        "campbell's dwarf hamster",
                        "male",
                        LocalDate.parse("2022-12-29"),
                        "under medical treatment",
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

        when(mapper.toDtoWithoutAdopter((Hamster) any()))
                .thenReturn(new HamsterDtoWithoutAdopter(
                        1L,
                        "Bolyhos",
                        HamsterSpecies.CAMPBELL,
                        Gender.MALE,
                        LocalDate.parse("2022-12-29"),
                        HamsterStatus.UNDER_MEDICAL_TREATMENT,
                        LocalDate.parse("2023-01-02"),
                        hostDtoWithoutHamsters,
                        "short desc"));

        HamsterDtoWithoutAdopter hamster = service.findAdoptableHamsterById(1L);

        assertThat(hamster.getName()).isEqualTo("Bolyhos");

        verify(hamsterRepository).findById(anyLong());
    }

    @Test
    void testFindHamsterByIdNotFound() {
        assertThatThrownBy(() ->
                service.findAdoptableHamsterById(101L))
                .isInstanceOf(HamsterWithIdNotExistException.class)
                .hasMessage("The hamster with the given ID (101) is not exist.");
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
                                "dawn",
                                Gender.MALE,
                                LocalDate.parse("2022-12-29"),
                                HamsterStatus.ADOPTED,
                                hostDtoWithoutHamsters,
                                LocalDate.parse("2023-01-02"),
                                adopterDtoWithoutHamsters,
                                LocalDate.parse("2023-01-02"),
                                "short desc"),
                        new HamsterDto(
                                1L,
                                "Füles",
                                HamsterSpecies.CAMPBELL,
                                "dawn",
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
                        HamsterStatus.ADOPTED, host, LocalDate.parse("2023-01-02"),
                        adopter, LocalDate.parse("2023-01-02"), "short desc");
        Hamster ham2 = new Hamster(1L,"Boholyka", HamsterSpecies.DWARF, Gender.MALE, LocalDate.parse("2022-12-29"),
                        HamsterStatus.ADOPTED, host, LocalDate.parse("2023-01-02"),
                        adopter, LocalDate.parse("2023-01-02"), "short desc");

        when(hamsterRepository.findHamsterByNameContains(anyString()))
                .thenReturn(List.of(ham1, ham2));
        when(mapper.toDto((List<Hamster>) any()))
                .thenReturn(List.of(
                        new HamsterDto(
                                1L,
                                "Bolyhos",
                                HamsterSpecies.CAMPBELL,
                                "dawn",
                                Gender.MALE,
                                LocalDate.parse("2022-12-29"),
                                HamsterStatus.ADOPTED,
                                hostDtoWithoutHamsters,
                                LocalDate.parse("2023-01-02"),
                                adopterDtoWithoutHamsters,
                                LocalDate.parse("2023-01-02"),"short desc"),
                        new HamsterDto(
                                1L,
                                "Boholyka",
                                HamsterSpecies.CAMPBELL,
                                "dawn",
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
                .hasMessage("The hamster with the given name (Kacsa) is not exit.");
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

        when(adopterRepository.findAdopterByIdWithHamsters(anyLong())).thenReturn(adopter);

        when(mapper.toDto((Hamster) any()))
                .thenReturn(new HamsterDto(
                        1L,
                        "Bolyhos",
                        HamsterSpecies.CAMPBELL,
                        "dawn",
                        Gender.MALE,
                        LocalDate.parse("2022-12-29"),
                        HamsterStatus.ADOPTED,
                        hostDtoWithoutHamsters,
                        LocalDate.parse("2023-01-02"),
                        adopterDtoWithoutHamsters,
                        LocalDate.parse("2023-01-02"),
                        "short desc"));

        HamsterDto hamster = service.adoptHamster(1L,
                new AdoptHamsterCommand(
                        adopterDtoWithoutHamsters.getId(),
                        LocalDate.parse("2023-01-02")));

        assertThat(hamster.getAdopter()).isNotNull();
        assertThat(hamster.getHamsterStatus()).isEqualTo(HamsterStatus.ADOPTED);

        verify(hamsterRepository).findById(anyLong());
        verify(adopterRepository).findAdopterByIdWithHamsters(anyLong());
        verify(hamsterRepository).save(any());
    }

    @Test
    void testGetListActualFosteringHamsters() {

    }

}