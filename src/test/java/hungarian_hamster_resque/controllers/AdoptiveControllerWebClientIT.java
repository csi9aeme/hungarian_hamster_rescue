package hungarian_hamster_resque.controllers;

import hungarian_hamster_resque.dtos.*;
import hungarian_hamster_resque.enums.HamsterStatus;
import jdk.jfr.Description;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ProblemDetail;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = {"delete from hamsters", "delete from adoptives"})
public class AdoptiveControllerWebClientIT {

    @Autowired
    WebTestClient webClient;

    @Autowired
    AdoptiveController controller;

    @Test
    @Description("Create adoptive")
    void createAdoptive() {
        AdoptiveDtoWithoutHamsters adoptive = webClient.post()
                .uri("/api/adoptives")
                .bodyValue(new CreateAdoptiveCommand("Zsíros B. Ödön", "7054 Tengelic, Alkotmány u. 32"))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(AdoptiveDtoWithoutHamsters.class).returnResult().getResponseBody();

        assertThat(adoptive.getId()).isNotNull();
    }

    @Test
    @Description("Exception: create adoptive with empty name")
    void createAdoptiveWithEmptyName() {
        ProblemDetail detail = webClient.post()
                .uri("/api/adoptives")
                .bodyValue(new CreateAdoptiveCommand("", "7054 Tengelic, Alkotmány u. 32"))
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertThat(detail.getType()).isEqualTo(URI.create("hamsterresque/not-valid"));
    }

    @Test
    @Description("Update adoptive's address")
    void updateAdoptive() {
        AdoptiveDtoWithoutHamsters adoptive = webClient.post()
                .uri("/api/adoptives")
                .bodyValue(new CreateAdoptiveCommand("Zsíros B. Ödön", "7054 Tengelic, Alkotmány u. 32"))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(AdoptiveDtoWithoutHamsters.class).returnResult().getResponseBody();
        long id = adoptive.getId();

        AdoptiveDtoWithoutHamsters updated = webClient.put()
                .uri("/api/adoptives/{id}", id)
                .bodyValue(new UpdateAdoptiveCommand("Zsíros B. Ödön", "6000 Kecskemét, Fő tér 6."))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(AdoptiveDtoWithoutHamsters.class).returnResult().getResponseBody();


        assertThat(updated.getAddress()).isEqualTo("6000 Kecskemét, Fő tér 6.");
    }

    @Test
    @Description("Get adoptives' list")
    void testGetAllAdoptives() {
        webClient.post().uri("api/adoptives")
                .bodyValue(new CreateAdoptiveCommand("Békési Klára", "Szeged"))
                .exchange()
                .expectStatus().isEqualTo(201);
        webClient.post().uri("api/adoptives")
                .bodyValue(new CreateAdoptiveCommand("Bogdán Klaudia", "Budapest"))
                .exchange()
                .expectStatus().isEqualTo(201);
        webClient.post().uri("api/adoptives")
                .bodyValue(new CreateAdoptiveCommand("Nagy Ernő", "Békéscsaba"))
                .exchange()
                .expectStatus().isEqualTo(201);

        List<AdoptiveDtoWithoutHamsters> result = webClient.get().uri("/api/adoptives")
                .exchange()
                .expectBodyList(AdoptiveDtoWithoutHamsters.class).returnResult().getResponseBody();

        assertThat(result)
                .hasSize(3)
                .extracting(AdoptiveDtoWithoutHamsters::getName)
                .containsExactly("Békési Klára", "Bogdán Klaudia", "Nagy Ernő");
    }

    @Test
    @Description("Get adoptives' list by name")
    void testGetAdoptivesListWithNamePart() {
        webClient.post().uri("api/adoptives")
                .bodyValue(new CreateAdoptiveCommand("Békési Klára", "Szeged"))
                .exchange()
                .expectStatus().isEqualTo(201);
        webClient.post().uri("api/adoptives")
                .bodyValue(new CreateAdoptiveCommand("Bogdán Klaudia", "Budapest"))
                .exchange()
                .expectStatus().isEqualTo(201);
        webClient.post().uri("api/adoptives")
                .bodyValue(new CreateAdoptiveCommand("Nagy Klaudia", "Békéscsaba"))
                .exchange()
                .expectStatus().isEqualTo(201);

        List<AdoptiveDtoWithoutHamsters> result = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/adoptives").queryParam("namePart", "Klaudia").build())
                .exchange()
                .expectBodyList(AdoptiveDtoWithoutHamsters.class).returnResult().getResponseBody();

        assertThat(result)
                .hasSize(2)
                .extracting(AdoptiveDtoWithoutHamsters::getName)
                .containsExactly("Bogdán Klaudia", "Nagy Klaudia");
    }

    @Test
    @Description("Exception: get adoptives' list with wrong name (empty list)")
    void testGetAdoptivesListWithWrongNamePart() {
        ProblemDetail detail = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/adoptives").queryParam("namePart", "Bence").build())
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertEquals(URI.create("hamsterresque/adoptive-not-found"), detail.getType());
    }

    @Test
    @Description("Get adoptives' list by city")
    void testGetAdoptivesListByCity() {
        webClient.post().uri("api/adoptives")
                .bodyValue(new CreateAdoptiveCommand("Békési Klára", "Szeged"))
                .exchange()
                .expectStatus().isEqualTo(201);
        webClient.post().uri("api/adoptives")
                .bodyValue(new CreateAdoptiveCommand("Bogdán Klaudia", "Budapest"))
                .exchange()
                .expectStatus().isEqualTo(201);
        webClient.post().uri("api/adoptives")
                .bodyValue(new CreateAdoptiveCommand("Nagy Klaudia", "Szeged"))
                .exchange()
                .expectStatus().isEqualTo(201);

        List<AdoptiveDtoWithoutHamsters> result = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/adoptives/adoptivesbycity").queryParam("city", "Szeged").build())
                .exchange()
                .expectBodyList(AdoptiveDtoWithoutHamsters.class).returnResult().getResponseBody();

        assertThat(result)
                .hasSize(2)
                .extracting(AdoptiveDtoWithoutHamsters::getName)
                .containsExactly("Békési Klára", "Nagy Klaudia");
    }

    @Test
    @Description("Exception: adoptive with city not exist")
    void testGetAdoptivesListByCityNotExist() {
        ProblemDetail detail = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/adoptives/adoptivesbycity").queryParam("city", "Békéscsaba")
                        .build())
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertThat(detail.getType()).isEqualTo(URI.create("hamsterresque/adoptive-not-found"));
    }

    @Test
    @Description("Find adoptive by id")
    void testFindAdoptiveById() {
        AdoptiveDtoWithoutHamsters adoptive = webClient.post().uri("api/adoptives")
                .bodyValue(new CreateAdoptiveCommand("Békési Klára", "Szeged"))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(AdoptiveDtoWithoutHamsters.class).returnResult().getResponseBody();
        long id = adoptive.getId();

        AdoptiveDtoWithoutHamsters result = webClient.get().uri("/api/adoptives/{id}", id)
                .exchange()
                .expectBody(AdoptiveDtoWithoutHamsters.class)
                .returnResult().getResponseBody();

        assertThat(result.getName()).isEqualTo("Békési Klára");
    }

    @Test
    @Description("Exception: find adoptive by invalid id")
    void testFindAdoptiveByIdInvalidId() {
        long id = 200;
        ProblemDetail detail = webClient.get().uri("/api/adoptives/{id}", id)
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertThat(detail.getType()).isEqualTo(URI.create("hamsterresque/adoptive-not-found"));
    }

    @Test
    @Description("Find adoptive by ID and hamsters")
    void testFindAdoptiveByIdWithHamsters(){
        HostDtoWithoutHamsters host = webClient.post().uri("api/hosts")
                .bodyValue(new CreateHostCommand("Békési Klára", "Szeged", 5, "aktív"))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(HostDtoWithoutHamsters.class).returnResult().getResponseBody();

        AdoptiveDtoWithoutHamsters adoptive = webClient.post().uri("api/adoptives")
                .bodyValue(new CreateAdoptiveCommand("Kiss Ernő", "Szeged"))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(AdoptiveDtoWithoutHamsters.class).returnResult().getResponseBody();

        HamsterDtoWithoutAdoptive hamster = webClient.post().uri("/api/hamsters")
                .bodyValue(new CreateHamsterCommand(
                        "Bolyhos",
                        "szíriai aranyhörcsög",
                        "nőstény",
                        LocalDate.parse("2022-11-01"),
                        "örökbefogadható",
                        host.getId(),
                        LocalDate.parse("2023-01-25"),
                        "short desc"))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(HamsterDtoWithoutAdoptive.class).returnResult().getResponseBody();

       webClient.put().uri("/api/hamsters/{id}/adopted", hamster.getId())
                .bodyValue(new AdoptHamsterCommand(adoptive.getId(), LocalDate.parse("2023-04-12")))
                .exchange()
                .expectBody(HamsterDto.class).returnResult().getResponseBody();

        AdoptiveDtoWithHamsters ownerWithHam = webClient.get().uri("/api/adoptives/{id}/hamsters", adoptive.getId())
                        .exchange()
                                .expectBody(AdoptiveDtoWithHamsters.class).returnResult().getResponseBody();
        assertThat(ownerWithHam.getHamsters())
                .hasSize(1)
                .extracting(HamsterDto::getName)
                .contains("Bolyhos");
    }
    @Test
    @Description("Delete adoptive by id")
    void testDeleteAdoptive() {
        AdoptiveDtoWithoutHamsters adoptive = webClient.post().uri("api/adoptives")
                .bodyValue(new CreateAdoptiveCommand("Kiss Ernő", "Szeged"))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(AdoptiveDtoWithoutHamsters.class).returnResult().getResponseBody();

        HostDtoWithoutHamsters host = webClient.post().uri("api/hosts")
                .bodyValue(new CreateHostCommand("Békési Klára", "Szeged", 5))
                .exchange()
                .expectBody(HostDtoWithoutHamsters.class).returnResult().getResponseBody();

       HamsterDtoWithoutAdoptive hamster = webClient.post().uri("/api/hamsters")
                .bodyValue(new CreateHamsterCommand(
                        "Bolyhos",
                        "dzsungáriai törpehörcsög",
                        "nőstény",
                        LocalDate.parse("2022-11-01"),
                        "örökbefogadható",
                        host.getId(),
                        LocalDate.parse("2023-01-25"),
                        "short desc"))
                .exchange()
               .expectBody(HamsterDtoWithoutAdoptive.class).returnResult().getResponseBody();

        webClient.put().uri("/api/hamsters/{id}/adopted", hamster.getId())
                .bodyValue(new AdoptHamsterCommand(adoptive.getId(), LocalDate.parse("2023-04-12")))
                .exchange();

        AdoptiveDtoWithHamsters adoptiveWithHam = webClient.get().uri("/api/adoptives/{id}/hamsters", adoptive.getId())
                .exchange().expectBody(AdoptiveDtoWithHamsters.class).returnResult().getResponseBody();

        ProblemDetail detail = webClient.delete().uri("api/adoptives/{id}", adoptive.getId())
                .exchange()
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertThat(detail.getType()).isEqualTo(URI.create("hamsterresque/adoptive-cant-delete"));

    }

    @Test
    @Description("Exception: delete adoptive by id with hamsters")
    void testDeleteAdoptiveWithHamsters() {
        AdoptiveDtoWithoutHamsters adoptive = webClient.post().uri("api/adoptives")
                .bodyValue(new CreateAdoptiveCommand("Kiss Ernő", "Szeged"))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(AdoptiveDtoWithoutHamsters.class).returnResult().getResponseBody();
        long id = adoptive.getId();

        webClient.delete().uri("api/adoptives/{id}", id)
                .exchange()
                .expectStatus().isEqualTo(204);

        ProblemDetail detail = webClient.get().uri("/api/adoptives/{id}", id)
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertThat(detail.getType()).isEqualTo(URI.create("hamsterresque/adoptive-not-found"));
    }
}




