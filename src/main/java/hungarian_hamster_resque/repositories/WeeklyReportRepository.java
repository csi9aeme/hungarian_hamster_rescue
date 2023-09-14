package hungarian_hamster_resque.repositories;


import hungarian_hamster_resque.models.WeeklyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeeklyReportRepository extends JpaRepository<WeeklyReport, Long> {

    @Query("select wr from weekly_reports wr order by dateOfMeasure desc")
    List<WeeklyReport> findAllReportsOrderDate();

    @Query("select wr from weekly_reports wr left join fetch  wr.hamster left join fetch wr.host where wr.host.id = :id order by dateOfMeasure desc")
    List<WeeklyReport> findByHostId(@Param("id") long id);

    @Query("select wr from weekly_reports wr left join fetch  wr.hamster left join fetch wr.host where wr.hamster.id = :id order by dateOfMeasure desc")
    List<WeeklyReport> findByHamsterId(@Param("id") long id);

}
