package hungarian_hamster_rescue.thymeleaf;

import hungarian_hamster_rescue.dtos.report.CreateReportCommand;
import hungarian_hamster_rescue.dtos.report.ReportDto;
import hungarian_hamster_rescue.services.WeeklyReportService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/reports")
@AllArgsConstructor
public class WeeklyReportThymeController {

    private  WeeklyReportService service;
    @GetMapping("/create-report")
    public String createReportForm(Model model) {
        CreateReportCommand report = new CreateReportCommand();
        model.addAttribute("report", report);


        return "reports/create-report-form";
    }
    @PostMapping("/create-report")
    public String submitForm(@ModelAttribute("report") CreateReportCommand report) {
        System.out.println(report);
        service.createReport(report);
        return "reports/create-report-success";
    }


    @GetMapping("/reports-of-host/{name}")
    public ModelAndView listOfReportsOfHostByName(@PathVariable("name") String name) {
        List<ReportDto> reports = service.getListOfReportsOfHostByName(name);

        Map<String, Object> model = Map.of();

        for (ReportDto r : reports) {
            model = Map.of(
                    "reports", reports);
        }
        return new ModelAndView("/reports/reports-of-host-by-name", model);

    }

    @GetMapping("/reports-of-hamster/{name}")
    public ModelAndView listOfReportsOfHamsterByName(@PathVariable("name") String name) {
        List<ReportDto> reports = service.getListOfReportsOfHamsterByName(name);

        Map<String, Object> model = Map.of(
                "hamsterName", reports.get(0).getHamsterName(),
                "reports", reports
        );

        return new ModelAndView("/reports/reports-of-hamster-by-name", model);

    }

}
