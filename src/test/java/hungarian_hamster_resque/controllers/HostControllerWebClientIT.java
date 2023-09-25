package hungarian_hamster_resque.controllers;

import hungarian_hamster_resque.dtos.AddressDto;
import hungarian_hamster_resque.dtos.ContactsDto;
import hungarian_hamster_resque.dtos.hamster.CreateHamsterCommand;
import hungarian_hamster_resque.dtos.hamster.HamsterDtoSimple;
import hungarian_hamster_resque.dtos.host.*;
import hungarian_hamster_resque.enums.HostStatus;
import hungarian_hamster_resque.repositories.HostRepository;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ProblemDetail;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.filter;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = {"delete from weekly_reports", "delete from hamsters", "delete from hosts"})
public class HostControllerWebClientIT {

    @Autowired
    HostController hostController;

    @Autowired
    WebTestClient webClient;

    CreateHostCommand klara;
    CreateHostCommand klaudia;
    CreateHostCommand erno;

    @Autowired
    private HostRepository hostRepository;

    @BeforeEach
    void init() {
        klara = new CreateHostCommand("Békési Klára", "6700", "Szeged", "Ősz utca", "7.", "", "+36201113333", "valami@gmail.com", "spzx", 5, "active");
        klaudia = new CreateHostCommand("Bogdán Klaudia", "1018", "Budapest", "Kiss Béla utca", "13.", "B","+36201113333", "valami@gmail.com", "spzx", 2, "active");
        erno = new CreateHostCommand("Nagy Ernő", "1191", "Budapest", "Újhegyi út", "70.", "2/7", "+36201113333", "valami@gmail.com", "spzx", 4, "active");
    }

    @Test
    @Description("Create new host")
    void testCreateHost() {
        HostDtoWithoutHamsters result = webClient.post().uri("api/hosts")
                .bodyValue(klara)
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(HostDtoWithoutHamsters.class).returnResult().getResponseBody();


        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("Békési Klára");
        assertThat(result.getAddressDto().getTown()).isEqualTo("Szeged");
        assertThat(result.getContactsDto().getPhoneNumber()).isEqualTo("+36201113333");
        assertThat(result.getContactsDto().getEmail()).isEqualTo("valami@gmail.com");

    }

    @Test
    @Description("Exception: create new host with empty name")
    void testCreateHostWithNoName() {
        ProblemDetail detail = webClient.post()
                .uri("/api/hosts")
                .bodyValue(new CreateHostCommand("", "1191", "Budapest", "Újegyi út", "70.", "2/7",
                        "+36201113333", "valami@gmail.com", "spzx", 3, "active"))
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertEquals(URI.create("hamsterresque/not-valid"), detail.getType());
        assertThat(detail.getDetail()).isEqualTo("Name cannot be empty!");
    }

    @Test
    @Description("Exception: create new host with invalid capacity")
    void testCreateHostWithoutCapacity() {
        ProblemDetail detail = webClient.post()
                .uri("/api/hosts")
                .bodyValue(new CreateHostCommand("Kis Boglárka", "1191", "Budapest", "Újegyi út", "70.", "2/7",
                        "+36201113333", "valami@gmail.com", "spzx", 0, "active"))
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertEquals(URI.create("hamsterresque/not-valid"), detail.getType());
        assertThat(detail.getDetail()).isEqualTo("Must be 1 or higher than 1.");
    }

    @Test
    @Description("Get hosts' list")
    void testGetHostsList() {
        webClient.post().uri("api/hosts")
                .bodyValue(klara)
                .exchange()
                .expectStatus().isEqualTo(201);
        webClient.post().uri("api/hosts")
                .bodyValue(klaudia)
                .exchange()
                .expectStatus().isEqualTo(201);
        webClient.post().uri("api/hosts")
                .bodyValue(erno)
                .exchange()
                .expectStatus().isEqualTo(201);

        List<HostDtoWithoutHamsters> result = webClient.get().uri("/api/hosts")
                .exchange()
                .expectBodyList(HostDtoWithoutHamsters.class).returnResult().getResponseBody();

        assertThat(result)
                .hasSize(3)
                .extracting(HostDtoWithoutHamsters::getName)
                .containsExactly("Békési Klára", "Bogdán Klaudia", "Nagy Ernő");
        assertThat(result)
                .extracting(HostDtoWithoutHamsters::getAddressDto)
                .extracting(AddressDto::getTown)
                .contains("Szeged");

        assertThat(result)
                .extracting(HostDtoWithoutHamsters::getContactsDto)
                .extracting(ContactsDto::getEmail)
                .contains("valami@gmail.com");


    }

    @Test
    @Description("Get hosts' list by name")
    void testGetHostsListWithNamePart() {
        webClient.post().uri("api/hosts")
                .bodyValue(klara)
                .exchange()
                .expectStatus().isEqualTo(201);
        webClient.post().uri("api/hosts")
                .bodyValue(klaudia)
                .exchange()
                .expectStatus().isEqualTo(201);
        webClient.post().uri("api/hosts")
                .bodyValue(erno)
                .exchange()
                .expectStatus().isEqualTo(201);

        List<HostDtoWithoutHamsters> result = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/hosts").queryParam("namePart", "Klára").build())
                .exchange()
                .expectBodyList(HostDtoWithoutHamsters.class).returnResult().getResponseBody();

        assertThat(result)
                .hasSize(1)
                .extracting(HostDtoWithoutHamsters::getAddressDto)
                .extracting(AddressDto::getTown)
                .contains("Szeged");


    }

    @Test
    @Description("Exception: get hosts' list with wrong name (empty list)")
    void testGetHostsListWithWrongNamePart() {
        ProblemDetail detail = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/hosts").queryParam("namePart", "Bence").build())
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertEquals(URI.create("hamsterresque/host-not-found"), detail.getType());
        assertThat(detail.getDetail()).isEqualTo("The temporary host with the given name (Bence) is not exit.");
    }

    @Test
    @Description("Find host by ID without hamsters' list")
    void testFindHostByIdWithoutHam() {
        HostDtoWithoutHamsters host = webClient.post().uri("api/hosts")
                .bodyValue(klara)
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(HostDtoWithoutHamsters.class).returnResult().getResponseBody();

        long id = host.getId();

        HostDtoWithoutHamsters result = webClient.get()
                .uri("/api/hosts/{id}", id)
                .exchange()
                .expectBody(HostDtoWithoutHamsters.class).returnResult().getResponseBody();

        assertThat(result.getName()).isEqualTo("Békési Klára");

    }

    @Test
    @Description("Exception: Find host by wrong ID")
    void testFindHostByIdWithoutHamWrongId() {
        long id = 2222;
        ProblemDetail detail = webClient.get()
                .uri("/api/hosts/{id}", id)
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertEquals(URI.create("hamsterresque/host-not-found"), detail.getType());
        assertThat(detail.getDetail()).isEqualTo("The temporary host with the given ID (" + id + ") is not exist.");

    }

    @Test
    @Description("Find host by ID and get the host's hamsters list")
    void testFindHostByIdWithHam() {
        HostDtoWithoutHamsters host = webClient.post().uri("api/hosts")
                .bodyValue(klara)
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(HostDtoWithoutHamsters.class).returnResult().getResponseBody();
        long id = host.getId();

        webClient.post().uri("/api/hamsters")
                .bodyValue(new CreateHamsterCommand(
                        "Bolyhos",
                        "golden hamster",
                        "gold",
                        "female",
                        LocalDate.parse("2022-11-01"),
                        "adoptable",
                        id,
                        LocalDate.parse("2023-01-25"),
                        "short desc"))
                .exchange().expectStatus().isEqualTo(201);

        HostDtoWithHamsters result = webClient.get()
                .uri("/api/hosts/{id}/hamsters", id)
                .exchange()
                .expectBody(HostDtoWithHamsters.class).returnResult().getResponseBody();

        assertThat(result.getHamsters())
                .isNotNull()
                .hasSize(1)
                .extracting(HamsterDtoSimple::getName)
                .containsExactly("Bolyhos");

    }

    @Test
    @Description("Exception: find a host by ID, but empty hamsters list")
    void testFindHostByIdWithEmptyHamList() {
        HostDtoWithoutHamsters host = webClient.post().uri("api/hosts")
                .bodyValue(klaudia)
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(HostDtoWithoutHamsters.class).returnResult().getResponseBody();
        long id = host.getId();

        ProblemDetail detail = webClient.get()
                .uri("/api/hosts/{id}/hamsters", id)
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertEquals(URI.create("hamsterresque/hamsters-not-found"), detail.getType());
        assertThat(detail.getDetail()).isEqualTo("The temporary host with the requested ID ("
                + host.getId() + ") does not currently have a hamster.");
    }

    @Test
    @Description("Update a host's address")
    void testUpdateHost() {
        HostDtoWithoutHamsters host = webClient.post().uri("api/hosts")
                .bodyValue(klara)
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(HostDtoWithoutHamsters.class).returnResult().getResponseBody();
        long id = host.getId();

        HostDtoWithoutHamsters updated = webClient.put().uri("api/hosts/{id}", id)
                .bodyValue(new UpdateHostCommand("Békési Klára", "1191", "Budapest", "Újhegyi út", "70.",
                        "2/7",  "+36201112222", "egyik@gmail.com", "skype", 4, HostStatus.ACTIVE))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(HostDtoWithoutHamsters.class).returnResult().getResponseBody();

        assertThat(updated.getAddressDto().getTown()).isEqualTo("Budapest");
        assertThat(updated.getContactsDto().getPhoneNumber()).isEqualTo("+36201112222");
    }

    @Test
    @Description("Get hosts' list by city")
    void testGetHostsByCity() {
        webClient.post().uri("api/hosts")
                .bodyValue(klara)
                .exchange()
                .expectStatus().isEqualTo(201);
        webClient.post().uri("api/hosts")
                .bodyValue(klaudia)
                .exchange()
                .expectStatus().isEqualTo(201);
        webClient.post().uri("api/hosts")
                .bodyValue(erno)
                .exchange()
                .expectStatus().isEqualTo(201);

        List<HostDtoWithHamsters> result = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/hosts/bycity").queryParam("city", "Budapest").build())
                .exchange()
                .expectBodyList(HostDtoWithHamsters.class).returnResult().getResponseBody();

        assertThat(result)
                .hasSize(2)
                .extracting(HostDtoWithHamsters::getName)
                .contains("Nagy Ernő");
    }

    @Test
    @Description("Exception: get hosts' list by wrong city")
    void testGetHostsByWrongCity() {
        webClient.post().uri("api/hosts")
                .bodyValue(klara)
                .exchange()
                .expectStatus().isEqualTo(201);
        webClient.post().uri("api/hosts")
                .bodyValue(klaudia)
                .exchange()
                .expectStatus().isEqualTo(201);
        webClient.post().uri("api/hosts")
                .bodyValue(erno)
                .exchange()
                .expectStatus().isEqualTo(201);


        ProblemDetail detail = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/hosts/bycity").queryParam("city", "Sopron").build())
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertEquals(URI.create("hamsterresque/host-not-found"), detail.getType());
    }


    @Test
    @Description("Set a host inactive")
    void testSetHostInactive() {
        HostDtoWithoutHamsters host = webClient.post().uri("api/hosts")
                .bodyValue(klaudia)
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(HostDtoWithoutHamsters.class).returnResult().getResponseBody();

        long id = host.getId();

        HostDtoWithoutHamsters hostGet = webClient.get().uri("api/hosts/{id}", id)
                .exchange()
                .expectBody(HostDtoWithoutHamsters.class).returnResult().getResponseBody();
        assertThat(hostGet.getHostStatus()).isEqualTo(HostStatus.ACTIVE);

        host = webClient.put().uri("api/hosts/{id}/inactive", id)
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(HostDtoWithoutHamsters.class).returnResult().getResponseBody();

        assertThat(host.getHostStatus()).isEqualTo(HostStatus.INACTIVE);

    }

    @Test
    void testGetListOfHostsOnlyWithFreeCapacity(){
        webClient.post().uri("api/hosts")
                .bodyValue(klara)
                .exchange()
                .expectStatus().isEqualTo(201);
        webClient.post().uri("api/hosts")
                .bodyValue(klaudia)
                .exchange()
                .expectStatus().isEqualTo(201);
        HostDtoWithoutHamsters hostDto = webClient.post().uri("api/hosts")
                .bodyValue(new CreateHostCommand("Nagy Ernő", "1191", "Budapest", "Újhegyi út", "70.", "2/7",
                        "+36201113333", "valami@gmail.com", "spzx", 1, "active"))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(HostDtoWithoutHamsters.class).returnResult().getResponseBody();

        Long id = hostDto.getId();

        assertThat(id).isNotNull();

        webClient.post().uri("/api/hamsters")
                .bodyValue(new CreateHamsterCommand(
                        "Bolyhos",
                        "djungarian dwarf hamster",
                        "dawn",
                        "female",
                        LocalDate.parse("2022-11-01"),
                        "adoptable",
                        id,
                        LocalDate.parse("2023-01-25"),
                        "short desc"))
                .exchange()
                .expectStatus().isEqualTo(201);

        List<HostDtoWithoutHamsters> resultAll = webClient.get().uri("/api/hosts")
                .exchange()
                .expectBodyList(HostDtoWithoutHamsters.class).returnResult().getResponseBody();


        List<HostDtoCountedCapacity> result = webClient.get().uri("/api/hosts/hostsOnlyWithFreeCapacity")
                .exchange()
                .expectBodyList(HostDtoCountedCapacity.class).returnResult().getResponseBody();

        assertThat(resultAll).hasSize(3);
        assertThat(result).hasSize(2)
                .extracting(HostDtoCountedCapacity::getName)
                .doesNotContain("Nagy Ernő");
        assertThat(result).extracting(HostDtoCountedCapacity::getContactsDto)
                .extracting(ContactsDto::getEmail)
                .contains("valami@gmail.com");
    }

    @Test
    @Description("Get hosts' list by city")
    void testGetHostsByCityOnlyWithFreeCapacity() {
        webClient.post().uri("api/hosts")
                .bodyValue(klara)
                .exchange()
                .expectStatus().isEqualTo(201);
        webClient.post().uri("api/hosts")
                .bodyValue(klaudia)
                .exchange()
                .expectStatus().isEqualTo(201);
        webClient.post().uri("api/hosts")
                .bodyValue(erno)
                .exchange()
                .expectStatus().isEqualTo(201);

        List<HostDtoWithHamsters> result = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/hosts/bycity").queryParam("city", "Budapest").build())
                .exchange()
                .expectBodyList(HostDtoWithHamsters.class).returnResult().getResponseBody();

        assertThat(result)
                .hasSize(2)
                .extracting(HostDtoWithHamsters::getName)
                .contains("Nagy Ernő");
    }



}