package hungarian_hamster_resque.controllers;

import hungarian_hamster_resque.dtos.*;
import hungarian_hamster_resque.dtos.CreateHamsterCommand;
import hungarian_hamster_resque.dtos.HamsterDtoSimple;
import hungarian_hamster_resque.enums.HamsterStatus;
import hungarian_hamster_resque.enums.HostStatus;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = {"delete from hamsters", "delete from hosts"})
public class HostControllerWebClientIT {

    @Autowired
    HostController hostController;

    @Autowired
    WebTestClient webClient;

    @Test
    @Description("Create new host")
    void testCreateHost() {
        HostDtoWithoutHamsters result = webClient.post().uri("api/hosts")
                .bodyValue(new CreateHostCommand("Békési Klára", "Szeged", 5))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(HostDtoWithoutHamsters.class).returnResult().getResponseBody();


        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("Békési Klára");

    }

    @Test
    @Description("Exception: create new host with empty name")
    void testCreateHostWithNoName() {
        ProblemDetail detail = webClient.post()
                .uri("/api/hosts")
                .bodyValue(new CreateHostCommand("", "Budapest", 3 ))
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertEquals(URI.create("hamsterresque/not-valid"), detail.getType());
    }

    @Test
    @Description("Exception: create new host with invalid capacity")
    void testCreateHostWithoutCapacity() {
        ProblemDetail detail = webClient.post()
                .uri("/api/hosts")
                .bodyValue(new CreateHostCommand("Kis Boglárka", "Budapest", 0))
                .exchange()
                .expectStatus().isEqualTo(406)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertEquals(URI.create("hamsterresque/not-valid"), detail.getType());
    }

    @Test
    @Description("Get hosts' list")
    void testGetHostsList() {
        webClient.post().uri("api/hosts")
                .bodyValue(new CreateHostCommand("Békési Klára", "Szeged", 5))
                .exchange()
                .expectStatus().isEqualTo(201);
        webClient.post().uri("api/hosts")
                .bodyValue(new CreateHostCommand("Bogdán Klaudia", "Budapest", 2))
                .exchange()
                .expectStatus().isEqualTo(201);
        webClient.post().uri("api/hosts")
                .bodyValue(new CreateHostCommand("Nagy Ernő", "Békéscsaba", 4))
                .exchange()
                .expectStatus().isEqualTo(201);

        List<HostDtoWithoutHamsters> result = webClient.get().uri("/api/hosts")
                .exchange()
                .expectBodyList(HostDtoWithoutHamsters.class).returnResult().getResponseBody();

        assertThat(result)
                .hasSize(3)
                .extracting(HostDtoWithoutHamsters::getName)
                .containsExactly("Békési Klára", "Bogdán Klaudia", "Nagy Ernő");
    }

    @Test
    @Description("Get hosts' list by name")
    void testGetHostsListWithNamePart() {
        webClient.post().uri("api/hosts")
                .bodyValue(new CreateHostCommand("Békési Klára", "Szeged", 5))
                .exchange()
                .expectStatus().isEqualTo(201);
        webClient.post().uri("api/hosts")
                .bodyValue(new CreateHostCommand("Bogdán Klaudia", "Budapest", 2))
                .exchange()
                .expectStatus().isEqualTo(201);
        webClient.post().uri("api/hosts")
                .bodyValue(new CreateHostCommand("Nagy Ernő", "Békéscsaba", 4))
                .exchange()
                .expectStatus().isEqualTo(201);

        List<HostDtoWithoutHamsters> result = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/hosts").queryParam("namePart", "Klára").build())
                .exchange()
                .expectBodyList(HostDtoWithoutHamsters.class).returnResult().getResponseBody();

        assertThat(result)
                .hasSize(1)
                .extracting(HostDtoWithoutHamsters::getAddress)
                .containsExactly("Szeged");
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
    }

    @Test
    @Description("Find host by ID without hamsters' list")
    void testFindHostByIdWithoutHam() {
        HostDtoWithoutHamsters host = webClient.post().uri("api/hosts")
                .bodyValue(new CreateHostCommand("Békési Klára", "Szeged", 5))
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
        ProblemDetail detail = webClient.get()
                .uri("/api/hosts/22")
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ProblemDetail.class).returnResult().getResponseBody();

        assertEquals(URI.create("hamsterresque/host-not-found"), detail.getType());

    }

    @Test
    @Description("Find host by ID and get the host's hamsters list")
    void testFindHostByIdWithHam() {
        HostDtoWithoutHamsters host = webClient.post().uri("api/hosts")
                .bodyValue(new CreateHostCommand("Békési Klára", "Szeged", 5))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(HostDtoWithoutHamsters.class).returnResult().getResponseBody();
        long id = host.getId();

        webClient.post().uri("/api/hamsters")
                .bodyValue(new CreateHamsterCommand(
                        "Bolyhos",
                        "dzsungáriai törpehörcsög",
                        "nőstény",
                        LocalDate.parse("2022-11-01"),
                        "örökbefogadható",
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
                .bodyValue(new CreateHostCommand("Békési Klára", "Szeged", 5))
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
    }

    @Test
    @Description("Update a host's address")
    void testUpdateHost() {
        HostDtoWithoutHamsters host = webClient.post().uri("api/hosts")
                .bodyValue(new CreateHostCommand("Békési Klára", "Szeged", 5))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(HostDtoWithoutHamsters.class).returnResult().getResponseBody();
        long id = host.getId();

        HostDtoWithoutHamsters updated = webClient.put().uri("api/hosts/{id}", id)
                .bodyValue(new UpdateHostCommand("Békési Klára", "Budapest", 4))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(HostDtoWithoutHamsters.class).returnResult().getResponseBody();

        assertThat(updated.getAddress()).isEqualTo("Budapest");
    }

    @Test
    @Description("Get hosts' list by city")
    void testGetHostsByCity() {
        webClient.post().uri("api/hosts")
                .bodyValue(new CreateHostCommand("Békési Klára", "Szeged", 5))
                .exchange()
                .expectStatus().isEqualTo(201);
        webClient.post().uri("api/hosts")
                .bodyValue(new CreateHostCommand("Bogdán Klaudia", "Budapest", 2))
                .exchange()
                .expectStatus().isEqualTo(201);
        webClient.post().uri("api/hosts")
                .bodyValue(new CreateHostCommand("Nagy Ernő", "Budapest", 4))
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
                .bodyValue(new CreateHostCommand("Békési Klára", "Szeged", 5))
                .exchange()
                .expectStatus().isEqualTo(201);
        webClient.post().uri("api/hosts")
                .bodyValue(new CreateHostCommand("Bogdán Klaudia", "Budapest", 2))
                .exchange()
                .expectStatus().isEqualTo(201);
        webClient.post().uri("api/hosts")
                .bodyValue(new CreateHostCommand("Nagy Ernő", "Budapest", 4))
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
                .bodyValue(new CreateHostCommand("Békési Klára", "Szeged", 5))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(HostDtoWithoutHamsters.class).returnResult().getResponseBody();

        long id = host.getId();

        HostDtoWithoutHamsters hostGet = webClient.get().uri("api/hosts/{id}", id)
                .exchange()
                .expectBody(HostDtoWithoutHamsters.class).returnResult().getResponseBody();
        System.out.println(hostGet.getHostStatus());
        assertThat(hostGet.getHostStatus()).isEqualTo(HostStatus.ACTIVE);



        host = webClient.put().uri("api/hosts/{id}/inactive", id)
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(HostDtoWithoutHamsters.class).returnResult().getResponseBody();

        assertThat(host.getHostStatus()).isEqualTo(HostStatus.INACTIVE);

    }
}