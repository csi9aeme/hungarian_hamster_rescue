package hungarian_hamster_rescue.units;

import hungarian_hamster_rescue.enums.Gender;
import hungarian_hamster_rescue.enums.HamsterSpecies;
import hungarian_hamster_rescue.enums.HamsterStatus;
import hungarian_hamster_rescue.enums.HostStatus;
import hungarian_hamster_rescue.models.Address;
import hungarian_hamster_rescue.models.Contacts;
import hungarian_hamster_rescue.models.Hamster;
import hungarian_hamster_rescue.models.Host;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class HostTest {

    @Test
    void testCreate() {
        Host host = new Host("Kiss Rozália", new Address("6700", "Szeged", "Ősz utca" ,"7.",""),
                new Contacts("+36202345678", "valami@email.hu", ""), HostStatus.ACTIVE, 3,
                new ArrayList<>(), new ArrayList<>());

        String name = host.getName();

        assertThat(name).isEqualTo("Kiss Rozália");
    }

    @Test
    void addHamster() {
        Host host = new Host("Kiss Rozália", new Address("6700", "Szeged", "Ősz utca" ,"7.",""), new Contacts("+36202345678", "valami@email.hu", ""), HostStatus.ACTIVE, 3,
                new ArrayList<>(), new ArrayList<>());

        host.addHamster(new Hamster(2L, "Füles",
                HamsterSpecies.DWARF,
                "white",
                Gender.FEMALE,
                LocalDate.parse("2022-11-01"),
                HamsterStatus.ADOPTABLE,
                LocalDate.parse("2023-01-25"),
                host,
                "short description",
                new ArrayList<>(),
                new ArrayList<>()));

        assertThat(host.getHamsters())
                .hasSize(1);
    }
}
