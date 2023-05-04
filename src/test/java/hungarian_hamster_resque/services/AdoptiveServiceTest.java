package hungarian_hamster_resque.services;

import hungarian_hamster_resque.dtos.*;
import hungarian_hamster_resque.enums.Gender;
import hungarian_hamster_resque.enums.HamsterSpecies;
import hungarian_hamster_resque.enums.HamsterStatus;
import hungarian_hamster_resque.exceptions.*;
import hungarian_hamster_resque.mappers.AdoptiveMapper;
import hungarian_hamster_resque.models.Adoptive;
import hungarian_hamster_resque.models.Hamster;
import hungarian_hamster_resque.models.Host;
import hungarian_hamster_resque.repositories.AdoptiveRepository;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

;

@ExtendWith(MockitoExtension.class)
class AdoptiveServiceTest {

    @Mock
    AdoptiveRepository repository;

    @Mock
    AdoptiveMapper mapper;

    @InjectMocks
    AdoptiveService service;

    @Test
    void testCreateAdoptive() {
        when(mapper.toDtoWithoutHamster((Adoptive) any()))
                .thenReturn(new AdoptiveDtoWithoutHamsters(1L, "Megyek Elemér", "1181 Budapest, Havanna utca 7."));

        AdoptiveDtoWithoutHamsters adoptive = service.createAdoptive(new CreateAdoptiveCommand("Megyek Elemér", "1181 Budapest, Havanna utca 7."));

        assertThat(adoptive.getId()).isNotNull();
        assertThat(adoptive.getName()).isEqualTo("Megyek Elemér");

        verify(repository).save(any());
    }

    @Test
    void testUpdateAdoptive() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(new Adoptive(1L, "Megyek Elemér", "1181 Budapest, Havanna utca 7.")));
        when(mapper.toDtoWithoutHamster((Adoptive) any()))
                .thenReturn(new AdoptiveDtoWithoutHamsters(1L, "Megyek Elemér", "1181 Budapest, Havanna utca 32."));

        AdoptiveDtoWithoutHamsters adoptive = service.updateAdoptive(1L,
                new UpdateAdoptiveCommand("Megyek Elemér", "1181 Budapest, Havanna utca 32."));

        assertThat(adoptive.getAddress()).isEqualTo("1181 Budapest, Havanna utca 32.");
        verify(repository).save(any());
        verify(repository).findById(any());

    }

    @Test
    void testGetAdoptives() {
        Adoptive adoptive1 = new Adoptive(1L, "Megyek Elemér", "1181 Budapest, Havanna utca 7.");
        Adoptive adoptive2 = new Adoptive(1L, "Hiszt Erika", "7400 Szekszárd, Fő utca 87.");

        when(repository.findAll())
                .thenReturn(List.of(adoptive1, adoptive2));
        when(mapper.toDtoWithoutHamster((List<Adoptive>) any()))
                .thenReturn(List.of(
                        new AdoptiveDtoWithoutHamsters(1L, "Megyek Elemér", "1181 Budapest, Havanna utca 7."),
                        new AdoptiveDtoWithoutHamsters(1L, "Hiszt Erika", "7400 Szekszárd, Fő utca 87.")));

        List<AdoptiveDtoWithoutHamsters> result = service.getAdoptives(Optional.empty());
        assertThat(result).hasSize(2);

        verify(repository).findAll();

    }

    @Test
    void testGetAdoptivesByName() {
        Adoptive adoptive1 = new Adoptive(1L, "Kiss Elemér", "1181 Budapest, Havanna utca 7.");
        Adoptive adoptive2 = new Adoptive(1L, "Kiss Erika", "7400 Szekszárd, Fő utca 87.");

        when(repository.findAdoptiveByNameContains(anyString()))
                .thenReturn(List.of(adoptive1, adoptive2));
        when(mapper.toDtoWithoutHamster(List.of(adoptive1, adoptive2)))
                .thenReturn(List.of(
                        new AdoptiveDtoWithoutHamsters(1L, "Kiss Elemér", "1181 Budapest, Havanna utca 7."),
                        new AdoptiveDtoWithoutHamsters(1L, "Kiss Erika", "7400 Szekszárd, Fő utca 87.")));

        List<AdoptiveDtoWithoutHamsters> result = service.getAdoptives(Optional.of("Kiss"));
        assertThat(result).hasSize(2);

        verify(repository).findAdoptiveByNameContains(anyString());

    }

    @Test
    void testGetAdoptivesByNotExistingName() {
        assertThatThrownBy(() ->
                service.getAdoptives(Optional.of("Kelemen")))
                .isInstanceOf(AdoptiveWithNameNotExistException.class)
                .hasMessage("A keresett névrészlettel (Kelemen) örökbefogadó nincs az adatbázisban.");

        verify(repository).findAdoptiveByNameContains(anyString());

    }

    @Test
    void testGetAdoptivesByCity() {
        Adoptive adoptive1 = new Adoptive(1L, "Megyek Elemér", "1181 Budapest, Havanna utca 7.");
        Adoptive adoptive2 = new Adoptive(1L, "Hiszt Erika", "1181 Budapest, Fő utca 87.");

        when(repository.findAdoptiveByAddressContains(anyString()))
                .thenReturn(List.of(adoptive1, adoptive2));
        when(mapper.toDtoWithoutHamster((List<Adoptive>) any()))
                .thenReturn(List.of(
                        new AdoptiveDtoWithoutHamsters(1L, "Megyek Elemér", "1181 Budapest, Havanna utca 7."),
                        new AdoptiveDtoWithoutHamsters(1L, "Hiszt Erika", "1181 Budapest, Fő utca 87.")));

        List<AdoptiveDtoWithoutHamsters> result = service.getAdoptivesByCity("Budapest");

        assertThat(result).hasSize(2);

        verify(repository).findAdoptiveByAddressContains(anyString());

    }

    @Test
    void testGetAdoptivesByWrongCityName() {
        assertThatThrownBy(() ->
                service.getAdoptivesByCity("Budapest"))
                .isInstanceOf(AdoptiveWithCityNotExistException.class)
                .hasMessage("A keresett városban (Budapest) jelenleg nincs örökbefogadó.");
        verify(repository).findAdoptiveByAddressContains(any());
    }

    @Test
    void testFindAdoptiveByIdWithHamsters() {
        Hamster ham1 = new Hamster( "Bolyhos", HamsterSpecies.DWARF, Gender.FEMALE, LocalDate.parse("2022-11-01"),
                HamsterStatus.ADOPTABLE, LocalDate.parse("2023-01-25"));
        Hamster ham2 = new Hamster("Füles", HamsterSpecies.DWARF, Gender.FEMALE, LocalDate.parse("2022-11-01"),
                HamsterStatus.ADOPTABLE,  LocalDate.parse("2023-01-25"));

        when(repository.findAdoptiveByIdWithHamsters(anyLong()))
                .thenReturn(new Adoptive(1L, "Megyek Elemér", "1181 Budapest, Havanna utca 7.",
                        List.of(ham1, ham2)));

        when(mapper.toDtoWithHamster((Adoptive) any()))
                .thenReturn(new AdoptiveDtoWithHamsters(
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

        AdoptiveDtoWithHamsters adoptive = service.findAdoptiveByIdWithHamsters(1L);

        assertThat(adoptive.getHamsters())
                .hasSize(2)
                .extracting(HamsterDto::getName)
                .contains("Füles");

        verify(repository).findAdoptiveByIdWithHamsters(anyLong());
    }

    @Test
    void testFindById() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(new Adoptive(1L, "Megyek Elemér", "1181 Budapest, Havanna utca 7.")));
        when(mapper.toDtoWithoutHamster((Adoptive) any()))
                .thenReturn(new AdoptiveDtoWithoutHamsters(1L, "Megyek Elemér", "1181 Budapest, Havanna utca 7."));

        AdoptiveDtoWithoutHamsters adoptive = service.findAdoptiveById(1L);
        assertThat(adoptive).isNotNull();
        assertThat(adoptive.getName()).isEqualTo("Megyek Elemér");

        verify(repository).findById(any());
    }

    @Test
    void testIdNotExist() {
        assertThatThrownBy(() ->
                service.findAdoptiveById(1102))
                .isInstanceOf(AdoptiveWithIdNotExistException.class)
                .hasMessage("A keresett ID-val (1102) örökbefogadó nincs az adatbázisban.");

        verify(repository).findById(any());
    }

    @Test
    void testDeleteAdoptive() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(new Adoptive(1L, "Megyek Elemér", "1181 Budapest, Havanna utca 7.")));
        doNothing().when(repository).deleteById(anyLong());

        service.deleteAdoptive(1L);

        verify(repository).deleteById(any());
    }

    @Test
    void testCantDeleteAdoptive() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(new Adoptive(1L, "Megyek Elemér", "1181 Budapest, Havanna utca 7.",
                        List.of(new Hamster("Füles", HamsterSpecies.DWARF, Gender.FEMALE, LocalDate.parse("2022-11-01"),
                                HamsterStatus.ADOPTABLE,  LocalDate.parse("2023-01-25"))))));

        assertThatThrownBy(() ->
                service.deleteAdoptive(1L))
                .isInstanceOf(AdoptiveCantDeleteBecauseHamstersListNotEmptyException.class)
                .hasMessage("A megadott ID-val (1) rendelkező örökbefogadó nem törölhető, merttartozik hozzá már örökbeadott hörcsög.");


        verify(repository).findById(any());
    }


}