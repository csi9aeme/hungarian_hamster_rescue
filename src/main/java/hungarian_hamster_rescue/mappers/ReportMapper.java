package hungarian_hamster_rescue.mappers;

import hungarian_hamster_rescue.dtos.report.ReportDto;
import hungarian_hamster_rescue.models.WeeklyReport;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReportMapper {

    ReportDto toReportDto(WeeklyReport weeklyReport);

    List<ReportDto> toReportDto(List<WeeklyReport> weeklyReports);



}
