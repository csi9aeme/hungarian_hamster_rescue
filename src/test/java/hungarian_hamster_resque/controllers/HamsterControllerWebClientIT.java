package hungarian_hamster_resque.controllers;

import hungarian_hamster_resque.dtos.*;
import hungarian_hamster_resque.enums.Gender;
import hungarian_hamster_resque.enums.HamsterSpecies;
import hungarian_hamster_resque.enums.HamsterStatus;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
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
import static org.junit.jupiter.api.Assertions.assertEquals;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = {"delete from hosts_hamsters", "delete from adoptives_hamsters",  "delete from hamsters",})
public class HamsterControllerWebClientIT {

    @Autowired
    WebTestClient webClient;

    @Autowired
    HamsterController hamsterController;

    HostDtoWithoutHamsters host;

    @BeforeEach
    void initHostAndAdoptive() {
        host = webClient.post().uri("api/hosts")
                .bodyValue(new CreateHostCommand("Békési Klára", "Szeged", 5))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(HostDtoWithoutHamsters.class).returnResult().getResponseBody();
    }


    @Test
    @Description("Create a new hamster")
    void testCreateHamster() {
        HamsterDtoWithoutAdoptive result = webClient.post().uri("/api/hamsters")
                .bodyValue(new CreateHamsterCommand(
                        "Bolyhos",
                        HamsterSpecies.DWARF,
                        Gender.FEMALE,
                        LocalDate.parse("2022-11-01"),
                        HamsterStatus.ADOPTABLE,
                        host.getId(),
                        LocalDate.parse("2023-01-25")))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(HamsterDtoWithoutAdoptive.class).returnResult().getResponseBody();

        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("Bolyhos");
        assertThat(result.getHamsterStatus()).isEqualTo(HamsterStatus.ADOPTABLE);
    }

    @Test
    @Description("Exception: create a new hamster with wrong name")
    void testCreateHamsterWithWrongName() {
        ProblemDetail detail = webClient.post()
                .uri("/api/hamsters")
                .bodyValue(new CreateHamsterCommand("",
                        HamsterSpecies.DWARF,
                        Gender.FEMALE,
                        LocalDate.parse("2022-11-01"),
                        HamsterStatus.ADOPTABLE,
                        host.getId(),
                        LocalDate.parse("2023-01-25")))
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertThat(detail.getType()).isEqualTo(URI.create("hamsterresque/not-valid"));
    }


    @Test
    @Description("Exception: create a new hamster with invalid host id")
    void testCreateHamsterWithInvalidHostId() {
        ProblemDetail detail = webClient.post()
                .uri("/api/hamsters")
                .bodyValue(new CreateHamsterCommand("",
                        HamsterSpecies.DWARF,
                        Gender.FEMALE,
                        LocalDate.parse("2022-11-01"),
                        HamsterStatus.ADOPTABLE,
                        22L,
                        LocalDate.parse("2023-01-25")))
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertThat(detail.getType()).isEqualTo(URI.create("hamsterresque/not-valid"));
    }

    @Test
    @Description("Get all hamsters")
    void testGetHamsters() {
        webClient.post().uri("/api/hamsters")
                .bodyValue(new CreateHamsterCommand(
                        "Bolyhos",
                        HamsterSpecies.DWARF,
                        Gender.FEMALE,
                        LocalDate.parse("2022-11-01"),
                        HamsterStatus.ADOPTABLE,
                        host.getId(),
                        LocalDate.parse("2023-01-25")))
                .exchange()
                .expectStatus().isEqualTo(201);
        webClient.post().uri("/api/hamsters")
                .bodyValue(new CreateHamsterCommand(
                        "Mütyürke",
                        HamsterSpecies.DWARF,
                        Gender.FEMALE,
                        LocalDate.parse("2022-11-01"),
                        HamsterStatus.ADOPTABLE,
                        host.getId(),
                        LocalDate.parse("2023-01-25")
                ))
                .exchange()
                .expectStatus().isEqualTo(201);


        List<HamsterDto> result = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/hamsters").queryParam("namePart", "").build())
                .exchange()
                .expectBodyList(HamsterDto.class).returnResult().getResponseBody();

        assertThat(result).hasSize(2);

    }

    @Test
    @Description("Get hamsters by namepart")
    void testGetHamstersByNamePart() {
        webClient.post().uri("/api/hamsters")
                .bodyValue(new CreateHamsterCommand(
                        "Bolyhos",
                        HamsterSpecies.DWARF,
                        Gender.FEMALE,
                        LocalDate.parse("2022-11-01"),
                        HamsterStatus.ADOPTABLE,
                        host.getId(),
                        LocalDate.parse("2023-01-25")))
                .exchange()
                .expectStatus().isEqualTo(201);
        webClient.post().uri("/api/hamsters")
                .bodyValue(new CreateHamsterCommand(
                        "Mütyürke",
                        HamsterSpecies.DWARF,
                        Gender.FEMALE,
                        LocalDate.parse("2022-11-01"),
                        HamsterStatus.ADOPTABLE,
                        host.getId(),
                        LocalDate.parse("2023-01-25")
                ))
                .exchange()
                .expectStatus().isEqualTo(201);


        List<HamsterDto> result = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/hamsters").queryParam("namePart", "Mü").build())
                .exchange()
                .expectBodyList(HamsterDto.class).returnResult().getResponseBody();

        assertThat(result).hasSize(1);

    }

    @Test
    @Description("Exception: get hamsters by invalid namepart")
    void testGetHamstersByInvalidNamePart() {
        ProblemDetail detail = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/hamsters").queryParam("namePart", "Mü").build())
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertThat(detail.getType()).isEqualTo(URI.create("hamsterresque/hamsters-not-found"));

    }

    @Test
    @Description("Exception: host can't take more hamster")
    void testHostCantTakeMoreHamster() {
       HostDtoWithoutHamsters newHost = webClient.post().uri("api/hosts")
                .bodyValue(new CreateHostCommand("Békési Klára", "Szeged", 1))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(HostDtoWithoutHamsters.class).returnResult().getResponseBody();

        webClient.post()
                .uri("/api/hamsters")
                .bodyValue(new CreateHamsterCommand(
                        "Mütyürke",
                        HamsterSpecies.DWARF,
                        Gender.FEMALE,
                        LocalDate.parse("2022-11-01"),
                        HamsterStatus.ADOPTABLE,
                        newHost.getId(),
                        LocalDate.parse("2023-01-25")))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        ProblemDetail detail = webClient.post()
                .uri("/api/hamsters")
                .bodyValue(new CreateHamsterCommand(
                "Bolyhos",
                HamsterSpecies.DWARF,
                Gender.FEMALE,
                LocalDate.parse("2022-11-01"),
                HamsterStatus.ADOPTABLE,
                newHost.getId(),
                LocalDate.parse("2023-01-25")))
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertThat(detail.getType()).isEqualTo(URI.create("hamsterresque/not-enough-capacity"));

    }
    @Test
    @Description("Get actual fostering hamsters")
    void testGetFosteringHamsters() {
        webClient.post().uri("/api/hamsters")
                .bodyValue(new CreateHamsterCommand(
                        "Bolyhos",
                        HamsterSpecies.DWARF,
                        Gender.FEMALE,
                        LocalDate.parse("2022-11-01"),
                        HamsterStatus.ADOPTABLE,
                        host.getId(),
                        LocalDate.parse("2023-01-25")))
                .exchange()
                .expectStatus().isEqualTo(201);

        webClient.post().uri("/api/hamsters")
                .bodyValue(new CreateHamsterCommand(
                        "Mütyürke",
                        HamsterSpecies.DWARF,
                        Gender.FEMALE,
                        LocalDate.parse("2022-11-01"),
                        HamsterStatus.ADOPTABLE,
                        host.getId(),
                        LocalDate.parse("2023-01-25")
                ))
                .exchange()
                .expectStatus().isEqualTo(201);

        webClient.post().uri("/api/hamsters")
                .bodyValue(new CreateHamsterCommand(
                        "Szotyi",
                        HamsterSpecies.DWARF,
                        Gender.FEMALE,
                        LocalDate.parse("2022-11-01"),
                        HamsterStatus.ADOPTED,
                        host.getId(),
                        LocalDate.parse("2023-01-25")
                ))
                .exchange()
                .expectStatus().isEqualTo(201);

        List<HamsterDto> result = webClient.get()
                .uri("/api/hamsters/fostering")
                .exchange()
                .expectBodyList(HamsterDto.class).returnResult().getResponseBody();

        assertThat(result).hasSize(2);
    }

    @Test
    @Description("Find hamster by ID")
    void testFindHamsterById() {
        HamsterDtoWithoutAdoptive hamster = webClient.post().uri("/api/hamsters")
                .bodyValue(new CreateHamsterCommand(
                        "Bolyhos",
                        HamsterSpecies.DWARF,
                        Gender.FEMALE,
                        LocalDate.parse("2022-11-01"),
                        HamsterStatus.ADOPTABLE,
                        host.getId(),
                        LocalDate.parse("2023-01-25")))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(HamsterDtoWithoutAdoptive.class).returnResult().getResponseBody();

        long id = hamster.getId();

        HamsterDtoWithoutAdoptive result = webClient.get().uri("/api/hamsters/{id}", id)
                .exchange()
                .expectBody(HamsterDtoWithoutAdoptive.class).returnResult().getResponseBody();

        assertThat(result.getName()).isEqualTo("Bolyhos");
    }

    @Test
    @Description("Exception: find hamster by invalid ID")
    void testFIndHamsterByInvalidId() {
        ProblemDetail detail = webClient.get().uri("/api/hamsters/{id}", 22)
                .exchange()
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertThat(detail.getType()).isEqualTo(URI.create("hamsterresque/hamster-not-found"));
    }

    @Test
    @Description("Update an existing hamster's attribute")
    void testUpdateHamster() {
        HamsterDtoWithoutAdoptive result = webClient.post().uri("/api/hamsters")
                .bodyValue(new CreateHamsterCommand(
                        "Bolyhos",
                        HamsterSpecies.DWARF,
                        Gender.FEMALE,
                        LocalDate.parse("2022-11-01"),
                        HamsterStatus.ADOPTABLE,
                        host.getId(),
                        LocalDate.parse("2023-01-25")))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(HamsterDtoWithoutAdoptive.class).returnResult().getResponseBody();
        long id = result.getId();

        HamsterDtoWithoutAdoptive updated = webClient.put().uri("/api/hamsters/{id}", id)
                .bodyValue(new UpdateHamsterCommand(
                        "Bolyhos",
                        HamsterSpecies.DWARF,
                        Gender.FEMALE,
                        LocalDate.parse("2022-11-01"),
                        HamsterStatus.ADOPTED,
                        host.getId(),
                        LocalDate.parse("2023-01-25")))
                .exchange()
                .expectBody(HamsterDtoWithoutAdoptive.class).returnResult().getResponseBody();

        assertThat(updated.getHamsterStatus()).isEqualTo(HamsterStatus.ADOPTED);
    }

    //kell hozzá az Adoptive

    @Test
    @Description("Adopt a hamster (change status and add an owner)")
    void testAdoptHamster() {
        AdoptiveDtoWithoutHamsters adoptive = webClient.post()
                .uri("/api/adoptives")
                .bodyValue(new CreateAdoptiveCommand("Zsíros B. Ödön", "7054 Tengelic, Alkotmány u. 32"))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(AdoptiveDtoWithoutHamsters.class).returnResult().getResponseBody();

        HamsterDtoWithoutAdoptive result = webClient.post().uri("/api/hamsters")
                .bodyValue(new CreateHamsterCommand(
                        "Bolyhos",
                        HamsterSpecies.DWARF,
                        Gender.FEMALE,
                        LocalDate.parse("2022-11-01"),
                        HamsterStatus.ADOPTABLE,
                        host.getId(),
                        LocalDate.parse("2023-01-25")))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(HamsterDtoWithoutAdoptive.class).returnResult().getResponseBody();
        long id = result.getId();

        HamsterDto adopted = webClient.put().uri("/api/hamsters/{id}/adopted", id)
                .bodyValue(new AdoptHamsterCommand(HamsterStatus.ADOPTED, adoptive.getId(), LocalDate.parse("2023-04-12")))
                .exchange()
                .expectBody(HamsterDto.class).returnResult().getResponseBody();

        assertThat(adopted.getHamsterStatus()).isEqualTo(HamsterStatus.ADOPTED);
        assertThat(adopted.getDateOfAdoption()).isEqualTo(LocalDate.parse("2023-04-12"));
    }

}
