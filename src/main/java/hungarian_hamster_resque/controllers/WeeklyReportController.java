package hungarian_hamster_resque.controllers;

import hungarian_hamster_resque.dtos.report.CreateReportCommand;
import hungarian_hamster_resque.dtos.report.ReportDto;
import hungarian_hamster_resque.services.WeeklyReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/reports")
@RequiredArgsConstructor
public class WeeklyReportController {

    private  final WeeklyReportService reportService;

    @PostMapping("/create-report")
    @ResponseStatus(HttpStatus.CREATED)
    public ReportDto createReport(@Valid @RequestBody CreateReportCommand command){
        return reportService.createReport(command);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.FOUND)
    public List<ReportDto> getAllReports(){
        return reportService.getAllReports();

    }
    @GetMapping("/reportsofhost/{hostid}")
    @ResponseStatus(HttpStatus.FOUND)
    public List<ReportDto> getListOfReportsOfHost(@PathVariable("hostid") long hostId){
        return reportService.getListOfReportsOfHost(hostId);

    }
    @GetMapping("/reportsofhostbyname/{name}")
    @ResponseStatus(HttpStatus.FOUND)
    public List<ReportDto> getListOfReportsOfHostByName(@PathVariable("name") String name){
        return reportService.getListOfReportsOfHostByName(name);

    }

    @GetMapping("/reportsofhamster/{hamsterid}")
    @ResponseStatus(HttpStatus.FOUND)
    public List<ReportDto> getListOfReportsOfHamster(@PathVariable("hamsterid") long hamsterId){
        return reportService.getListOfReportsOfHamster(hamsterId);
    }

    @GetMapping("/reportsofhamsterbyname/{name}")
    @ResponseStatus(HttpStatus.FOUND)
    public List<ReportDto> getListOfReportsOfHamsterByName(@PathVariable("name") String name){
        return reportService.getListOfReportsOfHamsterByName(name);
    }



}
