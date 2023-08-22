package hungarian_hamster_resque.mappers;

import hungarian_hamster_resque.dtos.report.ReportDto;
import hungarian_hamster_resque.models.WeeklyReport;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReportMapper {

    ReportDto toReportDto(WeeklyReport weeklyReport);

    List<ReportDto> toReportDto(List<WeeklyReport> weeklyReports);



}
