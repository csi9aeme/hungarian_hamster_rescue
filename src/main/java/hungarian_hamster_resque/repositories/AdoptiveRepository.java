package hungarian_hamster_resque.repositories;

import hungarian_hamster_resque.models.Adoptive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdoptiveRepository extends JpaRepository<Adoptive, Long> {

    List<Adoptive> findAdoptiveByNameContains(String s);

    List<Adoptive> findAdoptiveByAddressContains(String s);

    @Query("select adopt from Adoptive adopt left join fetch adopt.hamsters where adopt.id = :id")
    Adoptive findAdoptiveByIdWithHamsters(@Param("id") long id);
}