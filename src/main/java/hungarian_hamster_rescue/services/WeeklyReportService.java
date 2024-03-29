package hungarian_hamster_rescue.services;

import hungarian_hamster_rescue.dtos.report.CreateReportCommand;
import hungarian_hamster_rescue.dtos.report.ReportDto;
import hungarian_hamster_rescue.mappers.ReportMapper;
import hungarian_hamster_rescue.models.Hamster;
import hungarian_hamster_rescue.models.Host;
import hungarian_hamster_rescue.models.WeeklyReport;
import hungarian_hamster_rescue.repositories.HamsterRepository;
import hungarian_hamster_rescue.repositories.HostRepository;
import hungarian_hamster_rescue.repositories.WeeklyReportRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WeeklyReportService {


    private final ReportMapper reportMapper;

    private final HamsterRepository hamsterRepository;
    private final WeeklyReportRepository reportRepository;
    private final HostRepository hostRepository;


    public ReportDto createReport(CreateReportCommand command) {
        Hamster hamster = hamsterRepository.findCurrentHamsterByName(command.getHamsterName());
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
    @Transactional
    public List<ReportDto> getListOfReportsOfHostByName(String name) {
        Host host = hostRepository.findByNameWithoutHamsters(name);
        List<WeeklyReport> weeklyReports = reportRepository.findByHostId(host.getId());
        List<ReportDto> reportDto = getReportDtos(weeklyReports);
        return reportDto;
    }

    @Transactional
    public List<ReportDto> getListOfReportsOfHamster(long hamsterId) {
        List<WeeklyReport> weeklyReports = reportRepository.findByHamsterId(hamsterId);

        return getReportDtos(weeklyReports);
    }


    private List<ReportDto> getReportDtos(List<WeeklyReport> reports) {
        List<ReportDto> reportDto = reportMapper.toReportDto(reports);

        for(int i = 0; i < reportDto.size(); i++) {
            reportDto.get(i).setHamsterName(reports.get(i).getHamster().getName());
            reportDto.get(i).setHostName(reports.get(i).getHost().getName());
        }

        return reportDto;
    }


    public List<ReportDto> getListOfReportsOfHamsterByName(String name) {
        Hamster hamster = hamsterRepository.findCurrentHamsterByName(name);
        List<WeeklyReport> weeklyReports = reportRepository.findByHamsterId(hamster.getId());
        List<ReportDto> reportDto = getReportDtos(weeklyReports);
        return reportDto;
    }
}
