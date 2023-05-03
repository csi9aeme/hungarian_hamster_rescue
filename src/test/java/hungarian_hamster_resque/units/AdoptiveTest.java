package hungarian_hamster_resque.units;

import hungarian_hamster_resque.dtos.CreateHamsterCommand;
import hungarian_hamster_resque.dtos.CreateHostCommand;
import hungarian_hamster_resque.enums.Gender;
import hungarian_hamster_resque.enums.HamsterSpecies;
import hungarian_hamster_resque.enums.HamsterStatus;
import hungarian_hamster_resque.enums.HostStatus;
import hungarian_hamster_resque.models.Adoptive;
import hungarian_hamster_resque.models.Hamster;
import hungarian_hamster_resque.models.Host;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class AdoptiveTest {



    @Test
    void testCreate() {
        Adoptive adoptive = new Adoptive("Kiss Virág", "1092 Budapest, Fő utca 7.");
        String name = adoptive.getName();

        assertThat(name).isEqualTo("Kiss Virág");
    }

    @Test
    void testAddHamster() {
        Host host = new Host("Békési Klára", "Szeged", HostStatus.ACTIVE,  5);

        Adoptive adoptive = new Adoptive("Kiss Virág", "1092 Budapest, Fő utca 7.");
        adoptive.addHamster(new Hamster(
                "Bolyhos",
                HamsterSpecies.DWARF,
                Gender.FEMALE,
                LocalDate.parse("2022-11-01"),
                HamsterStatus.ADOPTABLE,
                host,
                LocalDate.parse("2023-01-25")));

        assertThat(adoptive.getHamsters())
                .hasSize(1)
                .extracting(Hamster::getName)
                .contains("Bolyhos");
    }
}