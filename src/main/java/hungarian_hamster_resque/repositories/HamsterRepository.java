package hungarian_hamster_resque.repositories;

import hungarian_hamster_resque.models.Hamster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HamsterRepository extends JpaRepository<Hamster, Long> {

    @Query("select ham from Hamster ham where ham.hamsterStatus != 'ADOPTED' AND ham.hamsterStatus != 'DECEASED'")
    List<Hamster> findFosteringHamsters();

    List<Hamster> findHamsterByNameContains(String s);

    @Query("select hamster from Hamster hamster where hamster.host.id = :id " +
            "AND hamster.hamsterStatus != 'ADOPTED' AND hamster.hamsterStatus != 'DECEASED'")
    List<Hamster> findFosteringHamstersByHostId(@Param("id") long id);


    @Query("select hamster.host.address from Hamster hamster where hamster.id = :id")
    String findHamsterPlace(@Param("id") long id);


    @Query("select hamster from Hamster hamster where hamster.name = :hamsterName AND " +
            "hamster.hamsterStatus != 'ADOPTED' AND hamster.hamsterStatus != 'DECEASED'")
    Hamster findCurrentHamsterByName(@Param("hamsterName") String hamsterName);


}
