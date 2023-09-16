package hungarian_hamster_resque.units;

import hungarian_hamster_resque.enums.Gender;
import hungarian_hamster_resque.enums.HamsterSpecies;
import hungarian_hamster_resque.enums.HamsterStatus;
import hungarian_hamster_resque.enums.HostStatus;
import hungarian_hamster_resque.models.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class AdopterTest {

    @Test
    void testCreate() {
        Adopter adopter = new Adopter("Kiss Virág", new Address("1092", "Budapest", "Fő utca", "7.", ""), new Contacts("+36302221111", "virag@gmail.com", ""));
        String name = adopter.getName();

        assertThat(name).isEqualTo("Kiss Virág");
    }

    @Test
    void testAddHamster() {
        Host host = new Host("Kiss Rozália", new Address("6700", "Szeged", "Ősz utca" ,"7.",""), HostStatus.ACTIVE, 3);


        Adopter adopter = new Adopter("Kiss Virág", new Address("1092", "Budapest", "Fő utca", "7.", ""));
        adopter.addHamster(new Hamster(
                "Bolyhos",
                HamsterSpecies.DWARF,
                Gender.FEMALE,
                LocalDate.parse("2022-11-01"),
                HamsterStatus.ADOPTABLE,
                host,
                LocalDate.parse("2023-01-25")));

        assertThat(adopter.getHamsters())
                .hasSize(1)
                .extracting(Hamster::getName)
                .contains("Bolyhos");
    }
}