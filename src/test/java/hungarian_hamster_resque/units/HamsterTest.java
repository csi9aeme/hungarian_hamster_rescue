package hungarian_hamster_resque.units;

import hungarian_hamster_resque.enums.Gender;
import hungarian_hamster_resque.enums.HamsterSpecies;
import hungarian_hamster_resque.enums.HamsterStatus;
import hungarian_hamster_resque.enums.HostStatus;
import hungarian_hamster_resque.models.Adoptive;
import hungarian_hamster_resque.models.Hamster;
import hungarian_hamster_resque.models.Host;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HamsterTest {

    Host host;

    @BeforeEach
    void init() {
         host = new Host("Békési Klára", "Szeged", HostStatus.ACTIVE,  5);

    }
    @Test
    void testCreate() {
        Hamster hamster = new Hamster("Bolyhos",
                HamsterSpecies.DWARF,
                Gender.FEMALE,
                LocalDate.parse("2022-11-01"),
                HamsterStatus.ADOPTABLE,
                host,
                LocalDate.parse("2023-01-25"));

        String name = hamster.getName();
        HamsterSpecies hamsterSpecies = hamster.getHamsterSpecies();
        Gender gender = hamster.getGender();

        assertThat(name).isEqualTo("Bolyhos");
        assertThat(hamsterSpecies).isEqualTo(HamsterSpecies.DWARF);
        assertThat(gender).isEqualTo(Gender.FEMALE);

    }


}