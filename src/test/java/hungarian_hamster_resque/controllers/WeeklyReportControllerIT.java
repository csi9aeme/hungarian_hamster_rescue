package hungarian_hamster_resque.controllers;

import hungarian_hamster_resque.dtos.*;
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

        host = webClient.post().uri("api/hosts")
                .bodyValue(new CreateHostCommand("Békési Klára", "Szeged", 5, "active", new ArrayList<>()))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(HostDtoWithoutHamsters.class).returnResult().getResponseBody();

        createHamster1 = new CreateHamsterCommand(
                "Bolyhos",
                "djungarian dwarf hamster",
                "female",
                LocalDate.parse("2022-11-01"),
                "adoptable",
                host.getId(),
                LocalDate.parse("2023-01-25"),
                "short desc");
        createHamster2 = new CreateHamsterCommand(
                "Mütyürke",
                "djungarian dwarf hamster",
                "female",
                LocalDate.parse("2022-11-01"),
                "adoptable",
                host.getId(),
                LocalDate.parse("2023-01-25"),
                "short desc");

        createHamster3 = new CreateHamsterCommand(
                "Szotyi",
                "djungarian dwarf hamster",
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

        createReportCommand1 = new CreateReportCommand(createHamster1.getName(), host.getName(), LocalDate.parse("2022-08-18"), 35.4, "Nothing has changed, everythingis ok.");
        createReportCommand2 = new CreateReportCommand(createHamster2.getName(), host.getName(), LocalDate.parse("2023-04-19"), 43, "Gain weight.");
        createReportCommand3 = new CreateReportCommand(createHamster3.getName(), host.getName(), LocalDate.parse("2021-06-13"), 50, "Less weight.");

    }

    @Test
    void testCreateReport(){
          ReportDto result = webClient.post().uri("/api/reports/create-report")
                .bodyValue(createReportCommand1)
                  .exchange()
                  .expectStatus().isEqualTo(201)
                  .expectBody(ReportDto.class).returnResult().getResponseBody();

          assertThat(result.getIdOfReport()).isNotNull();
          assertThat(result.getHamsterName()).isEqualTo("Bolyhos");

    }

    @Test
    void testGetALlOrderByDate(){
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
        assertThat(result.get(1).getDateOfMeasure()).isEqualTo(LocalDate.parse("2022-08-18"));
        assertThat(result.get(0).getDateOfMeasure()).isEqualTo(LocalDate.parse("2021-06-13"));

    }

    @Test
    void testGetWeeklyReportsByHostId(){
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
    void testGetWeeklyReportsOfHamster(){
       CreateReportCommand createReportCommand4 = new CreateReportCommand(createHamster1.getName(), host.getName(), LocalDate.parse("2023-04-19"), 43, "Gain weight.");
       CreateReportCommand createReportCommand5 = new CreateReportCommand(createHamster1.getName(), host.getName(), LocalDate.parse("2023-05-01"), 50, "Gain more weight.");

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
                .uri("/api/reports/reportsofhamsters/{hamsterid}", hamster.getId())
                .exchange()
                .expectBodyList(ReportDto.class).returnResult().getResponseBody();


        assertThat(result.size()).isEqualTo(3);


    }

}
