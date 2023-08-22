package hungarian_hamster_resque.dtos.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportDtoWithId {

    private Long idOfReport;

    private Long hamsterId;

    private Long hostId;

    private LocalDate dateOfMeasure;

    private double weight;

    private String reportText;


}
