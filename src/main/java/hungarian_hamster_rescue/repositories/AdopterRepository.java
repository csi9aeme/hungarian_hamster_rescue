package hungarian_hamster_rescue.repositories;

import hungarian_hamster_rescue.models.Adopter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdopterRepository extends JpaRepository<Adopter, Long> {

    List<Adopter> findAdopterByNameContains(String s);

    @Query("select a from Adopter a where a.address.town = :city")
    List<Adopter> findAdopterByCity(@Param("city") String s);

    @Query("select adopter from Adopter adopter left join fetch adopter.hamsters where adopter.id = :id")
    Adopter findAdopterByIdWithHamsters(@Param("id") long id);

}