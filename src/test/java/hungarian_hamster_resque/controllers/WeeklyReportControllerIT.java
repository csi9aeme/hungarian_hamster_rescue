package hungarian_hamster_resque.controllers;

import hungarian_hamster_resque.dtos.hamster.CreateHamsterCommand;
import hungarian_hamster_resque.dtos.hamster.HamsterDto;
import hungarian_hamster_resque.dtos.host.CreateHostCommand;
import hungarian_hamster_resque.dtos.host.HostDtoWithoutHamsters;
import hungarian_hamster_resque.dtos.report.CreateReportCommand;
import hungarian_hamster_resque.dtos.report.ReportDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = {"delete from weekly_reports", "delete from hamsters", "delete from hosts"})
public class WeeklyReportControllerIT {

    @Autowired
    WebTestClient webClient;

    CreateHostCommand createHostCommand1;
    HostDtoWithoutHamsters host;

    HamsterDto hamster;

    CreateHamsterCommand createHamster1;
    CreateHamsterCommand createHamster2;
    CreateHamsterCommand createHamster3;

    CreateReportCommand createReportCommand1;
    CreateReportCommand createReportCommand2;
    CreateReportCommand createReportCommand3;

    @BeforeEach
    void init() {
        createHostCommand1 = new CreateHostCommand("Békési Klára", "6700", "Szeged", "Fő utca", "7.", "", 5, "active", new ArrayList<>());
        host = webClient.post().uri("api/hosts")
                .bodyValue(createHostCommand1)
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(HostDtoWithoutHamsters.class).returnResult().getResponseBody();

        createHamster1 = new CreateHamsterCommand(
                "Bolyhos",
                "djungarian dwarf hamster",
                "blue",
                "female",
                LocalDate.parse("2022-11-01"),
                "adoptable",
                host.getId(),
                LocalDate.parse("2023-01-25"),
                "short desc");
        createHamster2 = new CreateHamsterCommand(
                "Mütyürke",
                "djungarian dwarf hamster",
                "pearl blue",
                "female",
                LocalDate.parse("2022-11-01"),
                "adoptable",
                host.getId(),
                LocalDate.parse("2023-01-25"),
                "short desc");

        createHamster3 = new CreateHamsterCommand(
                "Szotyi",
                "djungarian dwarf hamster",
                "pearl",
                "female",
                LocalDate.parse("2022-11-01"),
                "adoptable",
                host.getId(),
                LocalDate.parse("2023-01-25"),
                "short desc");

        hamster = webClient.post().uri("/api/hamsters")
                .bodyValue(createHamster1)
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(HamsterDto.class).returnResult().getResponseBody();
        webClient.post().uri("/api/hamsters")
                .bodyValue(createHamster2)
                .exchange()
                .expectStatus().isEqualTo(201);
        webClient.post().uri("/api/hamsters")
                .bodyValue(createHamster3)
                .exchange()
                .expectStatus().isEqualTo(201);

        createReportCommand1 = new CreateReportCommand(createHamster1.getName(), LocalDate.parse("2022-08-18"), 35.4, "Nothing has changed, everything is ok.");
        createReportCommand2 = new CreateReportCommand(createHamster2.getName(), LocalDate.parse("2023-04-19"), 43, "Gain weight.");
        createReportCommand3 = new CreateReportCommand(createHamster3.getName(), LocalDate.parse("2021-06-13"), 50, "Less weight.");

    }

    @Test
    void testCreateReport() {
        ReportDto result = webClient.post().uri("/api/reports/create-report")
                .bodyValue(createReportCommand1)
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(ReportDto.class).returnResult().getResponseBody();

        assertThat(result.getIdOfReport()).isNotNull();
        assertThat(result.getHamsterName()).isEqualTo("Bolyhos");

    }

    @Test
    void testGetALlOrderByDate() {
        webClient.post().uri("/api/reports/create-report")
                .bodyValue(createReportCommand1)
                .exchange()
                .expectStatus().isEqualTo(201);
        webClient.post().uri("/api/reports/create-report")
                .bodyValue(createReportCommand2)
                .exchange()
                .expectStatus().isEqualTo(201);
        webClient.post().uri("/api/reports/create-report")
                .bodyValue(createReportCommand3)
                .exchange()
                .expectStatus().isEqualTo(201);

        List<ReportDto> result = webClient.get()
                .uri("/api/reports/all")
                .exchange()
                .expectBodyList(ReportDto.class).returnResult().getResponseBody();

        assertThat(result.size()).isEqualTo(3);
        assertThat(result.get(0).getDateOfMeasure()).isEqualTo(LocalDate.parse("2023-04-19"));
        assertThat(result.get(1).getDateOfMeasure()).isEqualTo(LocalDate.parse("2022-08-18"));

    }

    @Test
    void testGetWeeklyReportsByHostId() {
        ReportDto dto1 = webClient.post().uri("/api/reports/create-report")
                .bodyValue(createReportCommand1)
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(ReportDto.class).returnResult().getResponseBody();
        ReportDto dto2 = webClient.post().uri("/api/reports/create-report")
                .bodyValue(createReportCommand2)
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(ReportDto.class).returnResult().getResponseBody();

        ReportDto dto3 = webClient.post().uri("/api/reports/create-report")
                .bodyValue(createReportCommand3)
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(ReportDto.class).returnResult().getResponseBody();


        List<ReportDto> result = webClient.get()
                .uri("/api/reports/reportsofhost/{hostid}", host.getId())
                .exchange()
                .expectBodyList(ReportDto.class).returnResult().getResponseBody();

        assertThat(result.size()).isEqualTo(3);
        assertThat(result).map(ReportDto::getHamsterName).contains("Bolyhos");

    }

    @Test
    void testGetWeeklyReportsByHostName() {
        ReportDto dto1 = webClient.post().uri("/api/reports/create-report")
                .bodyValue(createReportCommand1)
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(ReportDto.class).returnResult().getResponseBody();
        ReportDto dto2 = webClient.post().uri("/api/reports/create-report")
                .bodyValue(createReportCommand2)
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(ReportDto.class).returnResult().getResponseBody();

        ReportDto dto3 = webClient.post().uri("/api/reports/create-report")
                .bodyValue(createReportCommand3)
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(ReportDto.class).returnResult().getResponseBody();


        List<ReportDto> result = webClient.get()
                .uri("/api/reports/reportsofhostbyname/{name}", host.getName())
                .exchange()
                .expectBodyList(ReportDto.class).returnResult().getResponseBody();

        assertThat(result.size()).isEqualTo(3);
        assertThat(result).map(ReportDto::getHamsterName).contains("Bolyhos");

    }

    @Test
    void testGetWeeklyReportsOfHamster() {
        CreateReportCommand createReportCommand4 = new CreateReportCommand(createHamster1.getName(), LocalDate.parse("2023-04-19"), 43, "Gain weight.");
        CreateReportCommand createReportCommand5 = new CreateReportCommand(createHamster1.getName(), LocalDate.parse("2023-05-01"), 50, "Gain more weight.");

        ReportDto reportDto1 = webClient.post().uri("/api/reports/create-report")
                .bodyValue(createReportCommand1)
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(ReportDto.class).returnResult().getResponseBody();
        ReportDto reportDto2 = webClient.post().uri("/api/reports/create-report")
                .bodyValue(createReportCommand4)
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(ReportDto.class).returnResult().getResponseBody();
        ReportDto reportDto3 = webClient.post().uri("/api/reports/create-report")
                .bodyValue(createReportCommand5)
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(ReportDto.class).returnResult().getResponseBody();

        System.out.println(reportDto1.getReportText() + reportDto2.getReportText() + reportDto3.getReportText());
        List<ReportDto> result = webClient.get()
                .uri("/api/reports/reportsofhamster/{hamsterid}", hamster.getId())
                .exchange()
                .expectBodyList(ReportDto.class).returnResult().getResponseBody();

        assertThat(result.size()).isEqualTo(3);


    }

    @Test
    void testGetWeeklyReportsOfHamsterByName() {
        CreateReportCommand createReportCommand4 = new CreateReportCommand(createHamster1.getName(), LocalDate.parse("2023-04-19"), 43, "Gain weight.");
        CreateReportCommand createReportCommand5 = new CreateReportCommand(createHamster1.getName(), LocalDate.parse("2023-05-01"), 50, "Gain more weight.");

        webClient.post().uri("/api/reports/create-report")
                .bodyValue(createReportCommand1)
                .exchange()
                .expectStatus().isEqualTo(201);
        webClient.post().uri("/api/reports/create-report")
                .bodyValue(createReportCommand1)
                .exchange()
                .expectStatus().isEqualTo(201);
        webClient.post().uri("/api/reports/create-report")
                .bodyValue(createReportCommand1)
                .exchange()
                .expectStatus().isEqualTo(201);

        List<ReportDto> result = webClient.get()
                .uri("/api/reports/reportsofhamsterbyname/{name}", hamster.getName())
                .exchange()
                .expectBodyList(ReportDto.class).returnResult().getResponseBody();


        assertThat(result.size()).isEqualTo(3);


    }

}
