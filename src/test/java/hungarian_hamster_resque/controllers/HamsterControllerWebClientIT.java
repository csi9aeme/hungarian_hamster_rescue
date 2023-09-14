package hungarian_hamster_resque.controllers;

import hungarian_hamster_resque.dtos.adopter.AdoptHamsterCommand;
import hungarian_hamster_resque.dtos.adopter.AdopterDtoWithoutHamsters;
import hungarian_hamster_resque.dtos.adopter.CreateAdopterCommand;
import hungarian_hamster_resque.dtos.hamster.CreateHamsterCommand;
import hungarian_hamster_resque.dtos.hamster.HamsterDto;
import hungarian_hamster_resque.dtos.hamster.HamsterDtoWithoutAdopter;
import hungarian_hamster_resque.dtos.hamster.UpdateHamsterCommand;
import hungarian_hamster_resque.dtos.host.CreateHostCommand;
import hungarian_hamster_resque.dtos.host.HostDtoWithoutHamsters;
import hungarian_hamster_resque.enums.HamsterStatus;
import hungarian_hamster_resque.models.Address;
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
@Sql(statements = {"delete from weekly_reports", "delete from hamsters"})
public class HamsterControllerWebClientIT {

    @Autowired
    WebTestClient webClient;

    @Autowired
    HamsterController hamsterController;

    HostDtoWithoutHamsters host;

    CreateHostCommand createHostCommand1;
    CreateHostCommand createHostCommand2;

    CreateHamsterCommand createHamster1;
    CreateHamsterCommand createHamster2;
    CreateHamsterCommand createHamster3;

    @BeforeEach
    void initHostAndAdopter() {

        createHostCommand1 = new CreateHostCommand("Békési Klára", "6700", "Szeged", "Kis Pál utca", "3.", "", 5, "active");
        createHostCommand2 = new CreateHostCommand("Nagy Béla", "6700", "Szeged", "Kis Pál utca", "3.", "", 1, "active");

        host = webClient.post().uri("api/hosts")
                .bodyValue(createHostCommand1)
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(HostDtoWithoutHamsters.class).returnResult().getResponseBody();

        createHamster1 = new CreateHamsterCommand(
                "Bolyhos",
                "djungarian dwarf hamster",
                "dawn",
                "female",
                LocalDate.parse("2022-11-01"),
                "adoptable",
                host.getId(),
                LocalDate.parse("2023-01-25"),
                "short desc");
        createHamster2 = new CreateHamsterCommand(
                "Mütyürke",
                "djungarian dwarf hamster",
                "dawn",
                "female",
                LocalDate.parse("2022-11-01"),
                "adoptable",
                host.getId(),
                LocalDate.parse("2023-01-25"),
                "short desc");

        createHamster3 = new CreateHamsterCommand(
                "Szotyi",
                "djungarian dwarf hamster",
                "dawn",
                "female",
                LocalDate.parse("2022-11-01"),
                "adopted",
                host.getId(),
                LocalDate.parse("2023-01-25"),
                "short desc");
    }

    @Test
    @Description("Create a new hamster")
    void testCreateHamster() {
        HamsterDtoWithoutAdopter result = webClient.post().uri("/api/hamsters")
                .bodyValue(createHamster1)
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(HamsterDtoWithoutAdopter.class).returnResult().getResponseBody();

        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("Bolyhos");
        assertThat(result.getHamsterStatus()).isEqualTo(HamsterStatus.ADOPTABLE);
    }

    @Test
    @Description("Exception: create a new hamster with wrong name")
    void testCreateHamsterWithWrongName() {
        ProblemDetail detail = webClient.post()
                .uri("/api/hamsters")
                .bodyValue(new CreateHamsterCommand(
                        "",
                        "djungarian dwarf hamster",
                        "dawn",
                        "female",
                        LocalDate.parse("2022-11-01"),
                        "adoptable",
                        host.getId(),
                        LocalDate.parse("2023-01-25"),
                        "short desc"))
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertThat(detail.getType()).isEqualTo(URI.create("hamsterresque/not-valid"));
        assertThat(detail.getDetail()).isEqualTo("Name cannot be empty!");

    }

    @Test
    @Description("Exception: create a new hamster with wrong enum value")
    void testCreateHamsterWrongSpeciesValue() {
        ProblemDetail detail = webClient.post()
                .uri("/api/hamsters")
                .bodyValue(new CreateHamsterCommand(
                        "Bolyhos",
                        "djungarian ham",
                        "dawn",
                        "female",
                        LocalDate.parse("2022-11-01"),
                        "adoptable",
                        host.getId(),
                        LocalDate.parse("2023-01-25"),
                        "short desc"))
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertThat(detail.getType()).isEqualTo(URI.create("hamsterresque/hamsterspecies-not-exist"));
        assertThat(detail.getDetail()).isEqualTo("The given species (djungarian ham) is wrong.");
    }

    @Test
    @Description("Exception: create a new hamster with wrong gender value")
    void testCreateHamsterWrongGenderValue() {
        ProblemDetail detail = webClient.post()
                .uri("/api/hamsters")
                .bodyValue(new CreateHamsterCommand(
                        "Bolyhos",
                        "djungarian dwarf hamster",
                        "dawn",
                        "girl",
                        LocalDate.parse("2022-11-01"),
                        "adoptable",
                        host.getId(),
                        LocalDate.parse("2023-01-25"),
                        "short desc"))
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertThat(detail.getType()).isEqualTo(URI.create("hamsterresque/gender-not-acceptable"));
        assertThat(detail.getDetail()).isEqualTo("The given gender (girl) is wrong.");
    }

    @Test
    @Description("Exception: create a new hamster with wrong status value")
    void testCreateHamsterWrongStatusValue() {
        ProblemDetail detail = webClient.post()
                .uri("/api/hamsters")
                .bodyValue(new CreateHamsterCommand(
                        "Bolyhos",
                        "djungarian dwarf hamster",
                        "dawn",
                        "female",
                        LocalDate.parse("2022-11-01"),
                        "not adoptable",
                        host.getId(),
                        LocalDate.parse("2023-01-25"),
                        "short desc"))
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertThat(detail.getType()).isEqualTo(URI.create("hamsterresque/status-not-acceptable"));
        assertThat(detail.getDetail()).isEqualTo("The given status (not adoptable) is not acceptable.");
    }

    @Test
    @Description("Exception: create a new hamster with invalid host id")
    void testCreateHamsterWithInvalidHostId() {
        ProblemDetail detail = webClient.post()
                .uri("/api/hamsters")
                .bodyValue(new CreateHamsterCommand(
                        "Bolyhos",
                        "djungarian dwarf hamster",
                        "blue",
                        "female",
                        LocalDate.parse("2022-11-01"),
                        "adoptable",
                        1111111,
                        LocalDate.parse("2023-01-25"),
                        "short desc"))
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertThat(detail.getType()).isEqualTo(URI.create("hamsterresque/host-not-found"));
        assertThat(detail.getDetail()).isEqualTo("The temporary host with the given ID (1111111) is not exist.");

    }

    @Test
    @Description("Get all hamsters")
    void testGetHamsters() {
        webClient.post().uri("/api/hamsters")
                .bodyValue(createHamster1)
                .exchange()
                .expectStatus().isEqualTo(201);
        webClient.post().uri("/api/hamsters")
                .bodyValue(createHamster2)
                .exchange()
                .expectStatus().isEqualTo(201);


        List<HamsterDto> result = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/hamsters").queryParam("namePart", "").build())
                .exchange()
                .expectBodyList(HamsterDto.class).returnResult().getResponseBody();

        assertThat(result).hasSize(2);

    }

    @Test
    @Description("Get hamsters by name")
    void testGetHamstersByNamePart() {
        webClient.post().uri("/api/hamsters")
                .bodyValue(createHamster1)
                .exchange()
                .expectStatus().isEqualTo(201);
        webClient.post().uri("/api/hamsters")
                .bodyValue(createHamster2)
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
        assertThat(detail.getDetail()).isEqualTo("The hamster with the given name (Mü) is not exit.");

    }

    @Test
    @Description("Exception: host can't take more hamster")
    void testHostCantTakeMoreHamster() {
        HostDtoWithoutHamsters newHost = webClient.post().uri("api/hosts")
                .bodyValue(createHostCommand2)
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(HostDtoWithoutHamsters.class).returnResult().getResponseBody();

        webClient.post()
                .uri("/api/hamsters")
                .bodyValue(new CreateHamsterCommand(
                        "Bolyhos",
                        "djungarian dwarf hamster",
                        "blue",
                        "female",
                        LocalDate.parse("2022-11-01"),
                        "adoptable",
                        newHost.getId(),
                        LocalDate.parse("2023-01-25"),
                        "short desc"))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        ProblemDetail detail = webClient.post()
                .uri("/api/hamsters")
                .bodyValue(new CreateHamsterCommand(
                        "Mütyürke",
                        "djungarian dwarf hamster",
                        "blue",
                        "male",
                        LocalDate.parse("2022-11-01"),
                        "adoptable",
                        newHost.getId(),
                        LocalDate.parse("2023-01-25"),
                        "short desc"))
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertThat(detail.getType()).isEqualTo(URI.create("hamsterresque/not-enough-capacity"));
        assertThat(detail.getDetail()).isEqualTo("The temporary host with the given ID (" + newHost.getId() + ") can't take more hamster.");
    }

    @Test
    @Description("Exception: host status is inactive")
    void testHostIsInactive() {
        HostDtoWithoutHamsters newHost = webClient.post().uri("api/hosts")
                .bodyValue(createHostCommand1)
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(HostDtoWithoutHamsters.class).returnResult().getResponseBody();

        newHost = webClient.put().uri("api/hosts/{id}/inactive", newHost.getId())
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(HostDtoWithoutHamsters.class).returnResult().getResponseBody();

        ProblemDetail detail = webClient.post()
                .uri("/api/hamsters")
                .bodyValue(new CreateHamsterCommand(
                        "Bolyhos",
                        "djungarian dwarf hamster",
                        "blue",
                        "female",
                        LocalDate.parse("2022-11-01"),
                        "adoptable",
                        newHost.getId(),
                        LocalDate.parse("2023-01-25"),
                        "short desc"))
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertThat(detail.getType()).isEqualTo(URI.create("hamsterresque/inactive-host"));
        assertThat(detail.getDetail()).isEqualTo("The temporary host with the given ID (" + newHost.getId() + ") currently cannot take a hamster.");

    }

    @Test
    @Description("Get actual fostering hamsters")
    void testGetFosteringHamsters() {
        webClient.post().uri("/api/hamsters")
                .bodyValue(createHamster1)
                .exchange()
                .expectStatus().isEqualTo(201);

        webClient.post().uri("/api/hamsters")
                .bodyValue(createHamster2)
                .exchange()
                .expectStatus().isEqualTo(201);

        webClient.post().uri("/api/hamsters")
                .bodyValue(createHamster3)
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
        HamsterDtoWithoutAdopter hamster = webClient.post().uri("/api/hamsters")
                .bodyValue(createHamster1)
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(HamsterDtoWithoutAdopter.class).returnResult().getResponseBody();

        long id = hamster.getId();

        HamsterDtoWithoutAdopter result = webClient.get().uri("/api/hamsters/{id}", id)
                .exchange()
                .expectBody(HamsterDtoWithoutAdopter.class).returnResult().getResponseBody();

        assertThat(result.getName()).isEqualTo("Bolyhos");
    }

    @Test
    @Description("Exception: find hamster by invalid ID")
    void testFIndHamsterByInvalidId() {
        ProblemDetail detail = webClient.get().uri("/api/hamsters/{id}", 22222)
                .exchange()
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertThat(detail.getType()).isEqualTo(URI.create("hamsterresque/hamster-not-found"));
        assertThat(detail.getDetail()).isEqualTo("The hamster with the given ID (22222) is not exist.");

    }

    @Test
    @Description("Update an existing hamster's attribute")
    void testUpdateHamster() {
        HamsterDtoWithoutAdopter result = webClient.post().uri("/api/hamsters")
                .bodyValue(createHamster1)
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(HamsterDtoWithoutAdopter.class).returnResult().getResponseBody();
        long id = result.getId();

        HamsterDtoWithoutAdopter updated = webClient.put().uri("/api/hamsters/{id}", id)
                .bodyValue(new UpdateHamsterCommand(
                        "Bolyhos",
                        "djungarian dwarf hamster",
                        "female",
                        LocalDate.parse("2022-11-01"),
                        "under medical treatment",
                        host.getId(),
                        LocalDate.parse("2023-01-25")))
                .exchange()
                .expectBody(HamsterDtoWithoutAdopter.class).returnResult().getResponseBody();

        assertThat(updated.getHamsterStatus()).isEqualTo(HamsterStatus.UNDER_MEDICAL_TREATMENT);
    }

    @Test
    @Description("Adopt a hamster (change status and add an owner)")
    void testAdoptHamster() {
        CreateAdopterCommand adopterCommand = new CreateAdopterCommand("Zsíros B. Ödön", "7054", "Tengelic", "Alkotmány u.", "32", "");
        AdopterDtoWithoutHamsters adopter = webClient.post()
                .uri("/api/adopters")
                .bodyValue(adopterCommand)
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(AdopterDtoWithoutHamsters.class).returnResult().getResponseBody();

        HamsterDtoWithoutAdopter result = webClient.post().uri("/api/hamsters")
                .bodyValue(createHamster1)
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(HamsterDtoWithoutAdopter.class).returnResult().getResponseBody();
        long id = result.getId();

        HamsterDto adopted = webClient.put().uri("/api/hamsters/{id}/adopted", id)
                .bodyValue(new AdoptHamsterCommand(adopter.getId(), LocalDate.parse("2023-04-12")))
                .exchange()
                .expectBody(HamsterDto.class).returnResult().getResponseBody();

        assertThat(adopted.getHamsterStatus()).isEqualTo(HamsterStatus.ADOPTED);
        assertThat(adopted.getDateOfAdoption()).isEqualTo(LocalDate.parse("2023-04-12"));
    }

}
