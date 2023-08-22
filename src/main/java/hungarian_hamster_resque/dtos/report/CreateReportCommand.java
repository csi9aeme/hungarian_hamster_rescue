package hungarian_hamster_resque.dtos.report;


import hungarian_hamster_resque.models.Hamster;
import hungarian_hamster_resque.models.Host;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateReportCommand {

    private String hamsterName;

    private LocalDate dateOfMeasure;

    private double weight;

    private String reportText;
}
