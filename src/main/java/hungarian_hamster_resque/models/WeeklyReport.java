package hungarian_hamster_resque.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "weekly_reports")
public class WeeklyReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_of_report")
    private Long idOfReport;

    @ManyToOne(cascade = CascadeType.ALL)
    private Hamster hamster;

    @ManyToOne(cascade = CascadeType.ALL)
    private Host host;

    @Column(name = "date_of_measure")
    private LocalDate dateOfMeasure;

    private double weight;

    @Column(name = "report_text")
    private String reportText;


    public WeeklyReport(Hamster hamster, Host host, LocalDate dateOfMeasure, double weight, String reportText) {
        this.hamster = hamster;
        this.host = host;
        this.dateOfMeasure = dateOfMeasure;
        this.weight = weight;
        this.reportText = reportText;
    }
}
