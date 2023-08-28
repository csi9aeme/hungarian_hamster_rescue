package hungarian_hamster_resque.controllers;

import hungarian_hamster_resque.dtos.*;
import hungarian_hamster_resque.models.Host;
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

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = {"delete from hamsters", "delete from adopters"})
public class AdopterControllerWebClientIT {

    @Autowired
    WebTestClient webClient;

    @Autowired
    AdopterController controller;

    CreateAdopterCommand odon;
    CreateAdopterCommand klara;
    CreateAdopterCommand klaudiaKiss;
    CreateAdopterCommand klaudiaNagy;

    @BeforeEach
    void init() {
        odon = new CreateAdopterCommand("Zsíros B. Ödön", "7054 Tengelic, Alkotmány u. 32");
        klara = new CreateAdopterCommand("Békési Klára", "Szeged");
        klaudiaKiss = new CreateAdopterCommand("Kiss Klaudia", "Budapest");
        klaudiaNagy = new CreateAdopterCommand("Nagy Klaudia", "Szeged");

    }

    @Test
    @Description("Create adopter")
    void testCreateAdopter() {
        AdopterDtoWithoutHamsters adopter = webClient.post()
                .uri("/api/adopters")
                .bodyValue(odon)
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(AdopterDtoWithoutHamsters.class).returnResult().getResponseBody();

        assertThat(adopter.getId()).isNotNull();
    }

    @Test
    @Description("Exception: create adopter with empty name")
    void createAdopterWithEmptyName() {
        ProblemDetail detail = webClient.post()
                .uri("/api/adopters")
                .bodyValue(new CreateAdopterCommand("", "7054 Tengelic, Alkotmány u. 32"))
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertThat(detail.getType()).isEqualTo(URI.create("hamsterresque/not-valid"));
        assertThat(detail.getDetail()).isEqualTo("Name cannot be empty!");
    }

    @Test
    @Description("Update adopter's address")
    void updateAdopter() {
        AdopterDtoWithoutHamsters adopter = webClient.post()
                .uri("/api/adopters")
                .bodyValue(odon)
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(AdopterDtoWithoutHamsters.class).returnResult().getResponseBody();
        long id = adopter.getId();

        AdopterDtoWithoutHamsters updated = webClient.put()
                .uri("/api/adopters/{id}", id)
                .bodyValue(new UpdateAdopterCommand("Zsíros B. Ödön", "6000 Kecskemét, Fő tér 6."))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(AdopterDtoWithoutHamsters.class).returnResult().getResponseBody();


        assertThat(updated.getAddress()).isEqualTo("6000 Kecskemét, Fő tér 6.");
    }

    @Test
    @Description("Get adopters' list")
    void testGetAllAdopters() {
        webClient.post().uri("api/adopters")
                .bodyValue(klara)
                .exchange()
                .expectStatus().isEqualTo(201);
        webClient.post().uri("api/adopters")
                .bodyValue(klaudiaKiss)
                .exchange()
                .expectStatus().isEqualTo(201);
        webClient.post().uri("api/adopters")
                .bodyValue(odon)
                .exchange()
                .expectStatus().isEqualTo(201);

        List<AdopterDtoWithoutHamsters> result = webClient.get().uri("/api/adopters")
                .exchange()
                .expectBodyList(AdopterDtoWithoutHamsters.class).returnResult().getResponseBody();

        assertThat(result)
                .hasSize(3)
                .extracting(AdopterDtoWithoutHamsters::getName)
                .containsExactly("Békési Klára", "Kiss Klaudia", "Zsíros B. Ödön");
    }

    @Test
    @Description("Get adopters' list by name")
    void testGetAdoptersListWithNamePart() {
        webClient.post().uri("api/adopters")
                .bodyValue(klara)
                .exchange()
                .expectStatus().isEqualTo(201);
        webClient.post().uri("api/adopters")
                .bodyValue(klaudiaKiss)
                .exchange()
                .expectStatus().isEqualTo(201);
        webClient.post().uri("api/adopters")
                .bodyValue(klaudiaNagy)
                .exchange()
                .expectStatus().isEqualTo(201);

        List<AdopterDtoWithoutHamsters> result = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/adopters").queryParam("namePart", "Klaudia").build())
                .exchange()
                .expectBodyList(AdopterDtoWithoutHamsters.class).returnResult().getResponseBody();

        assertThat(result)
                .hasSize(2)
                .extracting(AdopterDtoWithoutHamsters::getName)
                .containsExactly("Kiss Klaudia", "Nagy Klaudia");
    }

    @Test
    @Description("Exception: get adopters' list with wrong name (empty list)")
    void testGetAdoptersListWithWrongNamePart() {
        ProblemDetail detail = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/adopters").queryParam("namePart", "Bence").build())
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertEquals(URI.create("hamsterresque/adopter-not-found"), detail.getType());
    }

    @Test
    @Description("Get adopters' list by city")
    void testGetAdoptersListByCity() {
        webClient.post().uri("api/adopters")
                .bodyValue(klara)
                .exchange()
                .expectStatus().isEqualTo(201);
        webClient.post().uri("api/adopters")
                .bodyValue(klaudiaKiss)
                .exchange()
                .expectStatus().isEqualTo(201);
        webClient.post().uri("api/adopters")
                .bodyValue(klaudiaNagy)
                .exchange()
                .expectStatus().isEqualTo(201);

        List<AdopterDtoWithoutHamsters> result = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/adopters/adoptersbycity").queryParam("city", "Szeged").build())
                .exchange()
                .expectBodyList(AdopterDtoWithoutHamsters.class).returnResult().getResponseBody();

        assertThat(result)
                .hasSize(2)
                .extracting(AdopterDtoWithoutHamsters::getName)
                .containsExactly("Békési Klára", "Nagy Klaudia");
    }

    @Test
    @Description("Exception: adopter with city not exist")
    void testGetAdoptersListByCityNotExist() {
        ProblemDetail detail = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/adopters/adoptersbycity").queryParam("city", "Békéscsaba")
                        .build())
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertThat(detail.getType()).isEqualTo(URI.create("hamsterresque/adopter-not-found"));
    }

    @Test
    @Description("Find adopter by id")
    void testFindAdopterById() {
        AdopterDtoWithoutHamsters adopter = webClient.post().uri("api/adopters")
                .bodyValue(new CreateAdopterCommand("Békési Klára", "Szeged"))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(AdopterDtoWithoutHamsters.class).returnResult().getResponseBody();
        long id = adopter.getId();

        AdopterDtoWithoutHamsters result = webClient.get().uri("/api/adopters/{id}", id)
                .exchange()
                .expectBody(AdopterDtoWithoutHamsters.class)
                .returnResult().getResponseBody();

        assertThat(result.getName()).isEqualTo("Békési Klára");
    }

    @Test
    @Description("Exception: find adopter by invalid id")
    void testFindAdopterByIdInvalidId() {
        long id = 200;
        ProblemDetail detail = webClient.get().uri("/api/adopters/{id}", id)
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertThat(detail.getType()).isEqualTo(URI.create("hamsterresque/adopter-not-found"));
    }

    @Test
    @Description("Find adopter by ID and hamsters")
    void testFindAdopterByIdWithHamsters(){
        HostDtoWithoutHamsters host = webClient.post().uri("api/hosts")
                .bodyValue(new CreateHostCommand("Békési Klára", "Szeged", 5, "active"))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(HostDtoWithoutHamsters.class).returnResult().getResponseBody();

        AdopterDtoWithoutHamsters adopter = webClient.post().uri("api/adopters")
                .bodyValue(odon)
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(AdopterDtoWithoutHamsters.class).returnResult().getResponseBody();

        HamsterDtoWithoutAdopter hamster = webClient.post().uri("/api/hamsters")
                .bodyValue(new CreateHamsterCommand(
                        "Bolyhos",
                        "golden",
                        "female",
                        LocalDate.parse("2022-11-01"),
                        "adoptable",
                        host.getId(),
                        LocalDate.parse("2023-01-25"),
                        "short desc"))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(HamsterDtoWithoutAdopter.class).returnResult().getResponseBody();

       webClient.put().uri("/api/hamsters/{id}/adopted", hamster.getId())
                .bodyValue(new AdoptHamsterCommand(adopter.getId(), LocalDate.parse("2023-04-12")))
                .exchange()
                .expectBody(HamsterDto.class).returnResult().getResponseBody();

        AdopterDtoWithHamsters ownerWithHam = webClient.get().uri("/api/adopters/{id}/hamsters", adopter.getId())
                        .exchange()
                                .expectBody(AdopterDtoWithHamsters.class).returnResult().getResponseBody();
        assertThat(ownerWithHam.getHamsters())
                .hasSize(1)
                .extracting(HamsterDto::getName)
                .contains("Bolyhos");
    }
    @Test
    @Description("Exception: Delete adopter by ID with hamsters")
    void testTryDeleteAdopterWithHamsters() {
        AdopterDtoWithoutHamsters adopter = webClient.post().uri("api/adopters")
                .bodyValue(odon)
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(AdopterDtoWithoutHamsters.class).returnResult().getResponseBody();

        HostDtoWithoutHamsters host = webClient.post().uri("api/hosts")
                .bodyValue(new CreateHostCommand("Békési Klára", "Szeged", 5, "active"))
                .exchange()
                .expectBody(HostDtoWithoutHamsters.class).returnResult().getResponseBody();

       HamsterDtoWithoutAdopter hamster = webClient.post().uri("/api/hamsters")
                .bodyValue(new CreateHamsterCommand(
                        "Bolyhos",
                        "golden",
                        "female",
                        LocalDate.parse("2022-11-01"),
                        "adoptable",
                        host.getId(),
                        LocalDate.parse("2023-01-25"),
                        "short desc"))
                .exchange()
               .expectBody(HamsterDtoWithoutAdopter.class).returnResult().getResponseBody();

        webClient.put().uri("/api/hamsters/{id}/adopted", hamster.getId())
                .bodyValue(new AdoptHamsterCommand(adopter.getId(), LocalDate.parse("2023-04-12")))
                .exchange();

        AdopterDtoWithHamsters adopterDtoWithHam = webClient.get().uri("/api/adopters/{id}/hamsters", adopter.getId())
                .exchange().expectBody(AdopterDtoWithHamsters.class).returnResult().getResponseBody();

        ProblemDetail detail = webClient.delete().uri("api/adopters/{id}", adopter.getId())
                .exchange()
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertThat(detail.getType()).isEqualTo(URI.create("hamsterresque/adopter-cant-delete"));
        assertThat(detail.getDetail()).isEqualTo("The adopter with the given ID (" +adopter.getId() +
                        ") cannot be deleted because it already has an adopted hamster.");

    }

    @Test
    @Description("Delete adopter by ID without hamsters")
    void testDeleteAdopterWithoutHamsters() {
        AdopterDtoWithoutHamsters adopter = webClient.post().uri("api/adopters")
                .bodyValue(new CreateAdopterCommand("Kiss Ernő", "Szeged"))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(AdopterDtoWithoutHamsters.class).returnResult().getResponseBody();
        long id = adopter.getId();

        webClient.delete().uri("api/adopters/{id}", id)
                .exchange()
                .expectStatus().isEqualTo(204);

        ProblemDetail detail = webClient.get().uri("/api/adopters/{id}", id)
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ProblemDetail.class)
                .returnResult().getResponseBody();

        assertThat(detail.getType()).isEqualTo(URI.create("hamsterresque/adopter-not-found"));

    }
}




