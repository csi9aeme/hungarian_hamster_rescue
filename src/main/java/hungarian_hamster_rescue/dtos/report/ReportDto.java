package hungarian_hamster_rescue.dtos.report;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportDto {

    private Long idOfReport;

    private String hamsterName;

    private String hostName;

    private LocalDate dateOfMeasure;

    private double weight;

    private String reportText;

    public ReportDto(String hamsterName, String hostName, LocalDate dateOfMeasure, double weight, String reportText) {
        this.hamsterName = hamsterName;
        this.hostName = hostName;
        this.dateOfMeasure = dateOfMeasure;
        this.weight = weight;
        this.reportText = reportText;
    }
}
