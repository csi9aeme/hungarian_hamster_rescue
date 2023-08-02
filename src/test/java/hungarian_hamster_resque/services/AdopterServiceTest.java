package hungarian_hamster_resque.services;

import hungarian_hamster_resque.dtos.*;
import hungarian_hamster_resque.enums.Gender;
import hungarian_hamster_resque.enums.HamsterSpecies;
import hungarian_hamster_resque.enums.HamsterStatus;
import hungarian_hamster_resque.exceptions.*;
import hungarian_hamster_resque.mappers.AdopterMapper;
import hungarian_hamster_resque.models.Adopter;
import hungarian_hamster_resque.models.Hamster;
import hungarian_hamster_resque.repositories.AdopterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

;

@ExtendWith(MockitoExtension.class)
class AdopterServiceTest {

    @Mock
    AdopterRepository repository;

    @Mock
    AdopterMapper mapper;

    @InjectMocks
    AdopterService service;

    @Test
    void testCreateAdopter() {
        when(mapper.toDtoWithoutHamster((Adopter) any()))
                .thenReturn(new AdopterDtoWithoutHamsters(1L, "Megyek Elemér", "1181 Budapest, Havanna utca 7."));

        AdopterDtoWithoutHamsters adopter = service.createAdopter(new CreateAdopterCommand("Megyek Elemér", "1181 Budapest, Havanna utca 7."));

        assertThat(adopter.getId()).isNotNull();
        assertThat(adopter.getName()).isEqualTo("Megyek Elemér");

        verify(repository).save(any());
    }

    @Test
    void testUpdateAdopter() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(new Adopter(1L, "Megyek Elemér", "1181 Budapest, Havanna utca 7.")));
        when(mapper.toDtoWithoutHamster((Adopter) any()))
                .thenReturn(new AdopterDtoWithoutHamsters(1L, "Megyek Elemér", "1181 Budapest, Havanna utca 32."));

        AdopterDtoWithoutHamsters adopter = service.updateAdopter(1L,
                new UpdateAdopterCommand("Megyek Elemér", "1181 Budapest, Havanna utca 32."));

        assertThat(adopter.getAddress()).isEqualTo("1181 Budapest, Havanna utca 32.");
        verify(repository).save(any());
        verify(repository).findById(any());

    }

    @Test
    void testGetAdopters() {
        Adopter adopter1 = new Adopter(1L, "Megyek Elemér", "1181 Budapest, Havanna utca 7.");
        Adopter adopter2 = new Adopter(1L, "Hiszt Erika", "7400 Szekszárd, Fő utca 87.");

        when(repository.findAll())
                .thenReturn(List.of(adopter1, adopter2));
        when(mapper.toDtoWithoutHamster((List<Adopter>) any()))
                .thenReturn(List.of(
                        new AdopterDtoWithoutHamsters(1L, "Megyek Elemér", "1181 Budapest, Havanna utca 7."),
                        new AdopterDtoWithoutHamsters(1L, "Hiszt Erika", "7400 Szekszárd, Fő utca 87.")));

        List<AdopterDtoWithoutHamsters> result = service.getAdopter(Optional.empty());
        assertThat(result).hasSize(2);

        verify(repository).findAll();

    }

    @Test
    void testGetAdoptersByName() {
        Adopter adopter1 = new Adopter(1L, "Kiss Elemér", "1181 Budapest, Havanna utca 7.");
        Adopter adopter2 = new Adopter(1L, "Kiss Erika", "7400 Szekszárd, Fő utca 87.");

        when(repository.findAdopterByNameContains(anyString()))
                .thenReturn(List.of(adopter1, adopter2));
        when(mapper.toDtoWithoutHamster(List.of(adopter1, adopter2)))
                .thenReturn(List.of(
                        new AdopterDtoWithoutHamsters(1L, "Kiss Elemér", "1181 Budapest, Havanna utca 7."),
                        new AdopterDtoWithoutHamsters(1L, "Kiss Erika", "7400 Szekszárd, Fő utca 87.")));

        List<AdopterDtoWithoutHamsters> result = service.getAdopter(Optional.of("Kiss"));
        assertThat(result).hasSize(2);

        verify(repository).findAdopterByNameContains(anyString());

    }

    @Test
    void testGetAdopterByNotExistingName() {
        assertThatThrownBy(() ->
                service.getAdopter(Optional.of("Kelemen")))
                .isInstanceOf(AdopterWithNameNotExistException.class)
                .hasMessage("A keresett névrészlettel (Kelemen) örökbefogadó nincs az adatbázisban.");

        verify(repository).findAdopterByNameContains(anyString());

    }

    @Test
    void testGetAdopterByCity() {
        Adopter adopter1 = new Adopter(1L, "Megyek Elemér", "1181 Budapest, Havanna utca 7.");
        Adopter adopter2 = new Adopter(1L, "Hiszt Erika", "1181 Budapest, Fő utca 87.");

        when(repository.findAdopterByAddressContains(anyString()))
                .thenReturn(List.of(adopter1, adopter2));
        when(mapper.toDtoWithoutHamster((List<Adopter>) any()))
                .thenReturn(List.of(
                        new AdopterDtoWithoutHamsters(1L, "Megyek Elemér", "1181 Budapest, Havanna utca 7."),
                        new AdopterDtoWithoutHamsters(1L, "Hiszt Erika", "1181 Budapest, Fő utca 87.")));

        List<AdopterDtoWithoutHamsters> result = service.getAdoptersByCity("Budapest");

        assertThat(result).hasSize(2);

        verify(repository).findAdopterByAddressContains(anyString());

    }

    @Test
    void testGetAdoptersByWrongCityName() {
        assertThatThrownBy(() ->
                service.getAdoptersByCity("Budapest"))
                .isInstanceOf(AdopterWithCityNotExistException.class)
                .hasMessage("A keresett városban (Budapest) jelenleg nincs örökbefogadó.");
        verify(repository).findAdopterByAddressContains(any());
    }

    @Test
    void testFindAdopterByIdWithHamsters() {
        Hamster ham1 = new Hamster( "Bolyhos", HamsterSpecies.DWARF, Gender.FEMALE, LocalDate.parse("2022-11-01"),
                HamsterStatus.ADOPTABLE, LocalDate.parse("2023-01-25"));
        Hamster ham2 = new Hamster("Füles", HamsterSpecies.DWARF, Gender.FEMALE, LocalDate.parse("2022-11-01"),
                HamsterStatus.ADOPTABLE,  LocalDate.parse("2023-01-25"));

        when(repository.findAdopterByIdWithHamsters(anyLong()))
                .thenReturn(new Adopter(1L, "Megyek Elemér", "1181 Budapest, Havanna utca 7.",
                        List.of(ham1, ham2)));

        when(mapper.toDtoWithHamster((Adopter) any()))
                .thenReturn(new AdopterDtoWithHamsters(
                       "Megyek Elemér", "1181 Budapest, Havanna utca 7.",
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
                .thenReturn(Optional.of(new Adopter(1L, "Megyek Elemér", "1181 Budapest, Havanna utca 7.")));
        when(mapper.toDtoWithoutHamster((Adopter) any()))
                .thenReturn(new AdopterDtoWithoutHamsters(1L, "Megyek Elemér", "1181 Budapest, Havanna utca 7."));

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
                .hasMessage("A keresett ID-val (1102) örökbefogadó nincs az adatbázisban.");

        verify(repository).findById(any());
    }

    @Test
    void testDeleteAdopter() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(new Adopter(1L, "Megyek Elemér", "1181 Budapest, Havanna utca 7.")));
        doNothing().when(repository).deleteById(anyLong());

        service.deleteAdopter(1L);

        verify(repository).deleteById(any());
    }

    @Test
    void testCantDeleteAdopter() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(new Adopter(1L, "Megyek Elemér", "1181 Budapest, Havanna utca 7.",
                        List.of(new Hamster("Füles", HamsterSpecies.DWARF, Gender.FEMALE, LocalDate.parse("2022-11-01"),
                                HamsterStatus.ADOPTABLE,  LocalDate.parse("2023-01-25"))))));

        assertThatThrownBy(() ->
                service.deleteAdopter(1L))
                .isInstanceOf(AdopterCantDeleteBecauseHamstersListNotEmptyException.class)
                .hasMessage("A megadott ID-val (1) rendelkező örökbefogadó nem törölhető, merttartozik hozzá már örökbeadott hörcsög.");


        verify(repository).findById(any());
    }


}