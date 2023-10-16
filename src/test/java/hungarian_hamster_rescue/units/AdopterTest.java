package hungarian_hamster_rescue.units;

import hungarian_hamster_rescue.enums.Gender;
import hungarian_hamster_rescue.enums.HamsterSpecies;
import hungarian_hamster_rescue.enums.HamsterStatus;
import hungarian_hamster_rescue.enums.HostStatus;
import hungarian_hamster_rescue.models.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class AdopterTest {


    @Test
    void testCreate() {
        Adopter adopter = new Adopter("Kiss Virág",
                new Address("1092", "Budapest", "Fő utca", "7.", ""),
                new Contacts("+36302221111", "virag@gmail.com", ""));

        String name = adopter.getName();

        assertThat(name).isEqualTo("Kiss Virág");
    }

    @Test
    void testAddHamster() {
        Host host = new Host(1L, "Kiss Rozália",
                new Address("6700", "Szeged", "Ősz utca" ,"7.",""),
                new Contacts("+36302221111", "virag@gmail.com", ""),
                HostStatus.ACTIVE,3, new ArrayList<>(), new ArrayList<>());

        Adopter adopter = new Adopter("Kiss Virág",
                new Address("1092", "Budapest", "Fő utca", "7.", ""),
                new Contacts("+36302221111", "virag@gmail.com", ""), new ArrayList<>());

        adopter.addHamster(new Hamster(
                1L, "Bolyhos",
                HamsterSpecies.DWARF,
                "blue pearl",
                Gender.FEMALE,
                LocalDate.parse("2022-11-01"),
                HamsterStatus.ADOPTABLE,
                LocalDate.parse("2023-01-25"),
                host,
                "short descreption of ham",
                new ArrayList<>(), new ArrayList<>()));

        assertThat(adopter.getHamsters())
                .hasSize(1)
                .extracting(Hamster::getName)
                .contains("Bolyhos");
    }
}