package hungarian_hamster_resque.repositories;

import hungarian_hamster_resque.models.Adopter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdopterRepository extends JpaRepository<Adopter, Long> {

    List<Adopter> findAdopterByNameContains(String s);

    List<Adopter> findAdopterByAddressContains(String s);

    @Query("select adopter from Adopter adopter left join fetch adopter.hamsters where adopter.id = :id")
    Adopter findAdopterByIdWithHamsters(@Param("id") long id);
}