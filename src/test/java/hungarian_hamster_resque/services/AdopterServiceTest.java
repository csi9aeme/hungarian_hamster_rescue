package hungarian_hamster_resque.services;

import hungarian_hamster_resque.dtos.AddressDto;
import hungarian_hamster_resque.dtos.adopter.AdopterDtoWithHamsters;
import hungarian_hamster_resque.dtos.adopter.AdopterDtoWithoutHamsters;
import hungarian_hamster_resque.dtos.adopter.CreateAdopterCommand;
import hungarian_hamster_resque.dtos.hamster.HamsterDto;
import hungarian_hamster_resque.dtos.adopter.UpdateAdopterCommand;
import hungarian_hamster_resque.enums.Gender;
import hungarian_hamster_resque.enums.HamsterSpecies;
import hungarian_hamster_resque.enums.HamsterStatus;
import hungarian_hamster_resque.exceptions.*;
import hungarian_hamster_resque.mappers.AddressMapper;
import hungarian_hamster_resque.mappers.AdopterMapper;
import hungarian_hamster_resque.models.Address;
import hungarian_hamster_resque.models.Adopter;
import hungarian_hamster_resque.models.Hamster;
import hungarian_hamster_resque.repositories.AdopterRepository;
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

    @Mock
    AddressMapper addressMapper;

    @InjectMocks
    AdopterService service;

    Adopter megyekElemerModel;
    Adopter adopterErika;

    AdopterDtoWithoutHamsters megyekElemerDto;
    AdopterDtoWithoutHamsters erikaDto;

    AdopterDtoWithHamsters megyekElemerDtoWithHamsters;

    CreateAdopterCommand createMegyekElemer;

    UpdateAdopterCommand updateMegyekElemer;

    @BeforeEach
    void init() {
        megyekElemerModel = new Adopter(1L, "Megyek Elemér", new Address("1181", "Budapest", "Havanna utca", "7.", ""));
        megyekElemerDto = new AdopterDtoWithoutHamsters( "Megyek Elemér", new AddressDto("1180", "Budapest", "Havanna utca", "8.", "Fsz.7."));
        megyekElemerDtoWithHamsters = new AdopterDtoWithHamsters( "Megyek Elemér", new AddressDto("1180", "Budapest", "Havanna utca", "8.", "Fsz.7."), new ArrayList<>());
        createMegyekElemer = new CreateAdopterCommand("Megyek Elemér", "1181", "Budapest", "Havanna utca", "7.", "");
        updateMegyekElemer = new UpdateAdopterCommand("Megyek Elemér", "1181", "Budapest", "Havanna utca", "7.", "");

        adopterErika = new Adopter(1L, "Hiszt Erika", new Address("7400", "Szekszárd", "Fő utca", "87.",""));
        erikaDto = new AdopterDtoWithoutHamsters( "Hiszt Erika", new AddressDto("7400", "Szekszárd", "Fő utca", "87.", ""));



    }
    @Test
    void testCreateAdopter() {
        when(adopterMapper.toDtoWithoutHamster((Adopter) any()))
                .thenReturn(megyekElemerDto);

        AdopterDtoWithoutHamsters adopter = service.createAdopter(createMegyekElemer);

        assertThat(adopter.getId()).isNotNull();
        assertThat(adopter.getName()).isEqualTo("Megyek Elemér");

        verify(repository).save(any());
    }

    @Test
    void testUpdateAdopter() {
        // Mockoljuk a hostRepository findById metódusát
        when(repository.findById(1L)).thenReturn(Optional.of(megyekElemerModel));

        // Mockoljuk az addressMapper toAddressDto metódusát
        when(addressMapper.toAddressDto((Address) any())).thenReturn(new AddressDto("1180", "Budapest", "Havanna utca", "8.", "Fsz.7."));

        // Teszteljük a metódust
        AdopterDtoWithoutHamsters result = service.updateAdopter(1L, updateMegyekElemer);

        // Ellenőrizzük az eredményt és a műveleteket
        assertEquals("Megyek Elemér", result.getName());
        assertEquals(megyekElemerDto.getAddress().getTown(), result.getAddress().getTown());

        // Ellenőrizzük, hogy a hostRepository save metódusa meghívódott
        verify(repository).save(megyekElemerModel);

    }

    @Test
    void testGetAdopters() {
        when(repository.findAll())
                .thenReturn(List.of(megyekElemerModel, adopterErika));
        when(adopterMapper.toDtoWithoutHamster((List<Adopter>) any()))
                .thenReturn(List.of(megyekElemerDto,erikaDto));

        List<AdopterDtoWithoutHamsters> result = service.getAdopter(Optional.empty());
        assertThat(result).hasSize(2);

        verify(repository).findAll();

    }

    @Test
    void testGetAdoptersByName() {
        Adopter adopter1 = new Adopter(1L, "Kiss Elemér", new Address("1181", "Budapest", "Havanna utca", "7.", ""));
        Adopter adopter2 = new Adopter(1L, "Kiss Erika", new Address("1181", "Budapest", "Havanna utca", "67.", "10/57"));

        when(repository.findAdopterByNameContains(anyString()))
                .thenReturn(List.of(adopter1, adopter2));
        when(adopterMapper.toDtoWithoutHamster(List.of(adopter1, adopter2)))
                .thenReturn(List.of(
                        new AdopterDtoWithoutHamsters(1L, "Kiss Elemér", new AddressDto("1181", "Budapest", "Havanna utca", "7.", "")),
                        new AdopterDtoWithoutHamsters(1L, "Kiss Erika", new AddressDto("1181", "Budapest", "Havanna utca", "67.", "10/57"))));

        List<AdopterDtoWithoutHamsters> result = service.getAdopter(Optional.of("Kiss"));
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
        Adopter adopter2 = new Adopter(1L, "Hiszt Erika", new Address("1181", "Budapest", "Havanna utca", "67.", "4/10"));
        AdopterDtoWithoutHamsters adopterDto2 = new AdopterDtoWithoutHamsters( "Hiszt Erika", new AddressDto("1181", "Budapest", "Havanna utca", "67.", "4/10"));

        when(repository.findAdopterByCity(anyString()))
                .thenReturn(List.of(megyekElemerModel, adopter2));
        when(adopterMapper.toDtoWithoutHamster((List<Adopter>) any()))
                .thenReturn(List.of(megyekElemerDto, adopterDto2));
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
        Hamster ham1 = new Hamster( "Bolyhos", HamsterSpecies.DWARF, Gender.FEMALE, LocalDate.parse("2022-11-01"),
                HamsterStatus.ADOPTABLE, LocalDate.parse("2023-01-25"));
        Hamster ham2 = new Hamster("Füles", HamsterSpecies.DWARF, Gender.FEMALE, LocalDate.parse("2022-11-01"),
                HamsterStatus.ADOPTABLE,  LocalDate.parse("2023-01-25"));

        when(repository.findAdopterByIdWithHamsters(anyLong()))
                .thenReturn(new Adopter(1L, "Megyek Elemér", new Address("1181", "Budapest", "Havanna utca", "7.", ""),
                        List.of(ham1, ham2)));

        when(adopterMapper.toDtoWithHamster((Adopter) any()))
                .thenReturn(new AdopterDtoWithHamsters(
                       "Megyek Elemér", new AddressDto("1181", "Budapest", "Havanna utca", "7.", ""),
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
                .thenReturn(Optional.of(new Adopter(1L, "Megyek Elemér", new Address("1181", "Budapest", "Havanna utca", "7.", ""),
                        List.of(new Hamster("Füles", HamsterSpecies.DWARF, Gender.FEMALE, LocalDate.parse("2022-11-01"),
                                HamsterStatus.ADOPTABLE,  LocalDate.parse("2023-01-25"))))));

        assertThatThrownBy(() ->
                service.deleteAdopter(1L))
                .isInstanceOf(AdopterCantDeleteBecauseHamstersListNotEmptyException.class)
                .hasMessage("The adopter with the given ID (1) cannot be deleted because it already has an adopted hamster.");


        verify(repository).findById(any());
    }


}