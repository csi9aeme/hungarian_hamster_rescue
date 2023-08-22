package hungarian_hamster_resque.services;

import hungarian_hamster_resque.dtos.report.CreateReportCommand;
import hungarian_hamster_resque.dtos.report.ReportDto;
import hungarian_hamster_resque.mappers.ReportMapper;
import hungarian_hamster_resque.models.Hamster;
import hungarian_hamster_resque.models.WeeklyReport;
import hungarian_hamster_resque.repositories.HamsterRepository;
import hungarian_hamster_resque.repositories.HostRepository;
import hungarian_hamster_resque.repositories.WeeklyReportRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeeklyReportService {


    private final ReportMapper reportMapper;

    private final HamsterRepository hamsterRepository;
    private final WeeklyReportRepository reportRepository;
    private final HostRepository hostRepository;


    public ReportDto createReport(CreateReportCommand command) {
        Hamster hamster = hamsterRepository.findCurrentHamsterByName(command.getHamsterName(), command.getHostName());
        WeeklyReport weeklyReport = WeeklyReport.builder()
                .hamster(hamster)
                .host(hamster.getHost())
                .dateOfMeasure(command.getDateOfMeasure())
                .weight(command.getWeight())
                .reportText(command.getReportText())
                .build();
        reportRepository.save(weeklyReport);

        ReportDto reportDto = reportMapper.toReportDto(weeklyReport);
        reportDto.setHamsterName(hamster.getName());
        reportDto.setHostName(hamster.getHost().getName());

        return reportDto;
    }

    public List<ReportDto> getAllReports() {
        List<WeeklyReport> weeklyReports = reportRepository.findAllReportsOrderDate();
        List<ReportDto> reportDto = getReportDtos(weeklyReports);
        return reportDto;
    }
    @Transactional
    public List<ReportDto> getListOfReportsOfHost(long id) {
        List<WeeklyReport> weeklyReports = reportRepository.findByHostId(id);
        List<ReportDto> reportDto = getReportDtos(weeklyReports);
        return reportDto;
    }

    public List<ReportDto> getListOfReportsOfHamster(long hamsterId) {
        List<WeeklyReport> weeklyReports = reportRepository.findByHamsterId(hamsterId);
        List<ReportDto> reportDto = getReportDtos(weeklyReports);
        return reportDto;
    }


    private List<ReportDto> getReportDtos(List<WeeklyReport> reports) {
        List<ReportDto> reportDto = reportMapper.toReportDto(reports);

        for(int i = 0; i < reportDto.size(); i++) {
            reportDto.get(i).setHamsterName(reports.get(i).getHamster().getName());
            reportDto.get(i).setHostName(reports.get(i).getHost().getName());
        }

        return reportDto;
    }



}
