package hungarian_hamster_rescue.units;

import hungarian_hamster_rescue.enums.Gender;
import hungarian_hamster_rescue.enums.HamsterSpecies;
import hungarian_hamster_rescue.enums.HamsterStatus;
import hungarian_hamster_rescue.enums.HostStatus;
import hungarian_hamster_rescue.models.Address;
import hungarian_hamster_rescue.models.Contacts;
import hungarian_hamster_rescue.models.Hamster;
import hungarian_hamster_rescue.models.Host;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class HamsterTest {

    Host host;

    @BeforeEach
    void init() {
        host = new Host("Kiss Rozália",
                new Address("6700", "Szeged", "Ősz utca" ,"7.",""),
                new Contacts("+36201112222", "valami@gmail.com", "skype"), HostStatus.ACTIVE, 3,
                new ArrayList<>(), new ArrayList<>());
    }
    @Test
    void testCreate() {
        Hamster hamster = new Hamster(2L, "Füles",
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

        String name = hamster.getName();
        HamsterSpecies hamsterSpecies = hamster.getHamsterSpecies();
        Gender gender = hamster.getGender();

        assertThat(name).isEqualTo("Füles");
        assertThat(hamsterSpecies).isEqualTo(HamsterSpecies.DWARF);
        assertThat(gender).isEqualTo(Gender.FEMALE);

    }


}