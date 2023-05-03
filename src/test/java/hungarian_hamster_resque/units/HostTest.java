package hungarian_hamster_resque.units;

import hungarian_hamster_resque.enums.Gender;
import hungarian_hamster_resque.enums.HamsterSpecies;
import hungarian_hamster_resque.enums.HamsterStatus;
import hungarian_hamster_resque.enums.HostStatus;
import hungarian_hamster_resque.models.Hamster;
import hungarian_hamster_resque.models.Host;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class HostTest {

    @Test
    void testCreate() {
        Host host = new Host("Kiss Rozália", "1092 Budapest, Fő utca 7.", HostStatus.ACTIVE, 3);
        String name = host.getName();

        assertThat(name).isEqualTo("Kiss Rozália");
    }

    @Test
    void addHamster() {
        Host host = new Host("Kiss Rozália", "1092 Budapest, Fő utca 7.", HostStatus.ACTIVE, 3);
        host.addHamster(new Hamster("Bolyhos",
                HamsterSpecies.DWARF,
                Gender.FEMALE,
                LocalDate.parse("2022-11-01"),
                HamsterStatus.ADOPTABLE,
                host,
                LocalDate.parse("2023-01-25")));

        assertThat(host.getHamsters())
                .hasSize(1);
    }
}
