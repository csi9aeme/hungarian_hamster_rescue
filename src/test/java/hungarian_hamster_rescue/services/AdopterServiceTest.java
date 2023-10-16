package hungarian_hamster_rescue.services;

import hungarian_hamster_rescue.dtos.AddressDto;
import hungarian_hamster_rescue.dtos.ContactsDto;
import hungarian_hamster_rescue.dtos.adopter.AdopterDtoWithHamsters;
import hungarian_hamster_rescue.dtos.adopter.AdopterDtoWithoutHamsters;
import hungarian_hamster_rescue.dtos.adopter.CreateAdopterCommand;
import hungarian_hamster_rescue.dtos.hamster.HamsterDto;
import hungarian_hamster_rescue.dtos.adopter.UpdateAdopterCommand;
import hungarian_hamster_rescue.enums.Gender;
import hungarian_hamster_rescue.enums.HamsterSpecies;
import hungarian_hamster_rescue.enums.HamsterStatus;
import hungarian_hamster_rescue.enums.HostStatus;
import hungarian_hamster_rescue.exceptions.*;
import hungarian_hamster_rescue.mappers.AdopterMapper;
import hungarian_hamster_rescue.models.*;
import hungarian_hamster_rescue.repositories.AdopterRepository;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AdopterServiceTest {

    @Mock
    AdopterRepository repository;

    @Mock
    AdopterMapper adopterMapper;



    @InjectMocks
    AdopterService service;

    Adopter megyekElemerModel;
    Adopter erikaModel;
    Adopter kissElemerModel;

    Host host;


    AdopterDtoWithoutHamsters megyekElemerDto;
    AdopterDtoWithoutHamsters erikaDto;
    AdopterDtoWithoutHamsters kissElemerDto;

    AdopterDtoWithHamsters megyekElemerDtoWithHamsters;

    CreateAdopterCommand createMegyekElemer;

    UpdateAdopterCommand updateMegyekElemer;

    Address addressBudapest1;
    Address addressBudapest2;
    Address addressSzeged1;
    Address addressSzeged2;

    AddressDto addressDtoBudapest1;
    AddressDto addressDtoBudapest2;
    AddressDto addressDtoSzeged1;
    AddressDto addressDtoSzeged2;

    Contacts contacts1;
    Contacts contacts2;

    ContactsDto contactsDto1;
    ContactsDto contactsDto2;

    @BeforeEach
    void init() {
        addressBudapest1 = new Address("1181", "Budapest", "Havanna utca", "7.", "");
        addressBudapest2 = new Address("1021", "Budapest", "Palota sétány", "98.", "C/5");
        addressSzeged1 = new Address("6700", "Szeged", "Rókus körút", "70.", "10/5");
        addressSzeged2 = new Address("6700", "Szeged", "Állomás tér", "7.", "10/5");

        addressDtoBudapest1 = new AddressDto("1181", "Budapest", "Havanna utca", "7.", "");
        addressDtoBudapest2 = new AddressDto("1021", "Budapest", "Palota sétány", "98.", "C/5");
        addressDtoSzeged1 = new AddressDto("6700", "Szeged", "Rókus körút", "70.", "10/5");
        addressDtoSzeged2 = new AddressDto("6700", "Szeged", "Állomás tér", "7.", "10/5");

        contacts1 = new Contacts("+36201112222", "egyik@gmail.com", "skype");
        contacts2 = new Contacts("+36201113333", "masik@gmail.com", "skype");

        contactsDto1 = new ContactsDto("+36201112222", "egyik@gmail.com", "skype");
        contactsDto2 = new ContactsDto("+36201113333", "masik@gmail.com", "skype");

        host = new Host(1L, "Kiss Klára", addressBudapest1, contacts1, HostStatus.ACTIVE, 1, new ArrayList<>(), new ArrayList<>());

        megyekElemerModel = new Adopter(1L, "Megyek Elemér", addressBudapest1, contacts1, new ArrayList<>());
        megyekElemerDto = new AdopterDtoWithoutHamsters( "Megyek Elemér", addressDtoBudapest1, contactsDto1);
        megyekElemerDtoWithHamsters = new AdopterDtoWithHamsters( "Megyek Elemér", addressDtoBudapest1, contactsDto1, new ArrayList<>());
        createMegyekElemer = new CreateAdopterCommand("Megyek Elemér", "1181", "Budapest", "Havanna utca", "7.", "", "+36201112222", "egyik@gmail.com", "skype");
        updateMegyekElemer = new UpdateAdopterCommand("Megyek Elemér", "1181", "Budapest", "Havanna utca", "7.", "", "+36201112222", "egyik@gmail.com", "skype");

        erikaModel = new Adopter(2L, "Hiszt Erika", addressSzeged1, contacts2, new ArrayList<>());
        erikaDto = new AdopterDtoWithoutHamsters( "Hiszt Erika", addressDtoSzeged1, contactsDto2);

        kissElemerModel = new Adopter(3L, "Kiss Elemer", addressBudapest2, contacts2, new ArrayList<>());
        kissElemerDto = new AdopterDtoWithoutHamsters( "Kiss Elemer", addressDtoBudapest2, contactsDto2);

    }
    @Test
    void testCreateAdopter() {
        when(adopterMapper.toDtoWithoutHamster((Adopter) any()))
                .thenReturn(megyekElemerDto);

        AdopterDtoWithoutHamsters adopter = service.createAdopter(createMegyekElemer);

        assertThat(adopter.getId()).isNotNull();
        assertThat(adopter.getName()).isEqualTo("Megyek Elemér");
        assertThat(adopter.getAddress().getTown()).isEqualTo("Budapest");
        assertThat(adopter.getContacts().getEmail()).isEqualTo("egyik@gmail.com");

        verify(repository).save(any());
    }

    @Test
    void testUpdateAdopter() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(megyekElemerModel));

        when(adopterMapper.toDtoWithoutHamster((Adopter) any())).thenReturn(megyekElemerDto);

        AdopterDtoWithoutHamsters result = service.updateAdopter(1L, updateMegyekElemer);

        assertEquals("Megyek Elemér", result.getName());
        assertEquals(megyekElemerDto.getAddress().getTown(), result.getAddress().getTown());

        verify(repository).save(megyekElemerModel);
        verify(repository).findById(anyLong());

    }

    @Test
    void testGetAdopters() {
        when(repository.findAll())
                .thenReturn(List.of(megyekElemerModel, erikaModel));
        when(adopterMapper.toDtoWithoutHamster((List<Adopter>) any()))
                .thenReturn(List.of(megyekElemerDto,erikaDto));

        List<AdopterDtoWithoutHamsters> result = service.getAdopter(Optional.empty());
        assertThat(result).hasSize(2);

        verify(repository).findAll();

    }

    @Test
    void testGetAdoptersByName() {
//        Adopter adopter1 = new Adopter(1L, "Kiss Elemér",
//                new Address("1181", "Budapest", "Havanna utca", "7.", "")
//                new Contacts("+36302221111", "virag@gmail.com", ""), new ArrayList<>());
//        Adopter adopter2 = new Adopter(1L, "Kiss Erika",
//                new Address("1181", "Budapest", "Havanna utca", "67.", "10/57"),
//                new Contacts("+36302221111", "virag@gmail.com", ""), new ArrayList<>());

        when(repository.findAdopterByNameContains(anyString()))
                .thenReturn(List.of(megyekElemerModel, kissElemerModel));
        when(adopterMapper.toDtoWithoutHamster(List.of(megyekElemerModel, kissElemerModel)))
                .thenReturn(List.of(kissElemerDto, megyekElemerDto));

        List<AdopterDtoWithoutHamsters> result = service.getAdopter(Optional.of("Elemer"));
        assertThat(result).hasSize(2);

        verify(repository).findAdopterByNameContains(anyString());

    }

    @Test
    void testGetAdopterByNotExistingName() {
        assertThatThrownBy(() ->
                service.getAdopter(Optional.of("Kelemen")))
                .isInstanceOf(AdopterWithNameNotExistException.class)
                .hasMessage("The adopter with the given name (Kelemen) not exist.");

        verify(repository).findAdopterByNameContains(anyString());

    }

    @Test
    void testGetAdopterByCity() {
        when(repository.findAdopterByCity(anyString()))
                .thenReturn(List.of(megyekElemerModel, kissElemerModel));
        when(adopterMapper.toDtoWithoutHamster((List<Adopter>) any()))
                .thenReturn(List.of(megyekElemerDto, kissElemerDto));
        List<AdopterDtoWithoutHamsters> result = service.getAdoptersByCity("Budapest");

        assertThat(result).hasSize(2);

        verify(repository).findAdopterByCity(anyString());

    }

    @Test
    void testGetAdoptersByWrongCityName() {
        assertThatThrownBy(() ->
                service.getAdoptersByCity("Budapest"))
                .isInstanceOf(AdopterWithCityNotExistException.class)
                .hasMessage("There are currently no adopters in the specified city: (Budapest).");
        verify(repository).findAdopterByCity(any());
    }

    @Test
    void testFindAdopterByIdWithHamsters() {
        Hamster ham1 = new Hamster(1L, "Füles",
                HamsterSpecies.DWARF,
                "white",
                Gender.FEMALE,
                LocalDate.parse("2022-11-01"),
                HamsterStatus.ADOPTABLE,
                LocalDate.parse("2023-01-25"),
                host,
                "short description",
                new ArrayList<>(),
                new ArrayList<>());
        Hamster ham2 = new Hamster(2L, "Bolyhos",
                HamsterSpecies.DWARF,
                "black",
                Gender.MALE,
                LocalDate.parse("2022-11-01"),
                HamsterStatus.ADOPTABLE,
                LocalDate.parse("2023-01-25"),
                host,
                "short description",
                new ArrayList<>(),
                new ArrayList<>());

        when(repository.findAdopterByIdWithHamsters(anyLong()))
                .thenReturn(new Adopter(1L, "Megyek Elemér", addressBudapest2, contacts2, List.of(ham1, ham2)));

        when(adopterMapper.toDtoWithHamster((Adopter) any()))
                .thenReturn(new AdopterDtoWithHamsters(
                       "Megyek Elemér", addressDtoBudapest1, contactsDto1,
                        List.of(new HamsterDto(1L, "Bolyhos",
                                        HamsterSpecies.DWARF,
                                        Gender.FEMALE,
                                        LocalDate.parse("2022-11-01"),
                                        null,
                                        LocalDate.parse("2023-01-25"),
                                        LocalDate.parse("2023-04-19")),
                                new HamsterDto(1L, "Füles",
                                        HamsterSpecies.DWARF,
                                        Gender.FEMALE,
                                        LocalDate.parse("2022-11-01"),
                                        null,
                                        LocalDate.parse("2023-02-25"),
                                        LocalDate.parse("2023-04-30")))));

        AdopterDtoWithHamsters adopter = service.findAdopterByIdWithHamsters(1L);

        assertThat(adopter.getHamsters())
                .hasSize(2)
                .extracting(HamsterDto::getName)
                .contains("Füles");

        verify(repository).findAdopterByIdWithHamsters(anyLong());
    }

    @Test
    void testFindAdopterById() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(megyekElemerModel));
        when(adopterMapper.toDtoWithoutHamster((Adopter) any()))
                .thenReturn(megyekElemerDto);

        AdopterDtoWithoutHamsters adopter = service.findAdopteryId(1L);
        assertThat(adopter).isNotNull();
        assertThat(adopter.getName()).isEqualTo("Megyek Elemér");

        verify(repository).findById(any());
    }

    @Test
    void testAdopterIdNotExist() {
        assertThatThrownBy(() ->
                service.findAdopteryId(1102))
                .isInstanceOf(AdopterWithIdNotExistException.class)
                .hasMessage("The adopter with the given ID (1102) not exist.");

        verify(repository).findById(any());
    }

    @Test
    void testDeleteAdopter() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(megyekElemerModel));
        doNothing().when(repository).deleteById(anyLong());

        service.deleteAdopter(1L);

        verify(repository).deleteById(any());
    }

    @Test
    void testCantDeleteAdopter() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(new Adopter(1L, "Megyek Elemér", addressBudapest2, contacts2,
                        List.of(new Hamster(1L, "Füles",
                                HamsterSpecies.DWARF,
                                "white",
                                Gender.FEMALE,
                                LocalDate.parse("2022-11-01"),
                                HamsterStatus.ADOPTABLE,
                                LocalDate.parse("2023-01-25"),
                                host,
                                "short description",
                                new ArrayList<>(),
                                new ArrayList<>())))));

        assertThatThrownBy(() ->
                service.deleteAdopter(1L))
                .isInstanceOf(AdopterCantDeleteBecauseHamstersListNotEmptyException.class)
                .hasMessage("The adopter with the given ID (1) cannot be deleted because it already has an adopted hamster.");


        verify(repository).findById(any());
    }


}