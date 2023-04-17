package hungarian_hamster_resque.repositories;

import hungarian_hamster_resque.models.Hamster;
import hungarian_hamster_resque.models.Host;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HostRepository extends JpaRepository<Host, Long> {

    @Query("select host from Host host left join fetch host.hamsters where host.name like concat('%',:namepart,'%')")
    List<Host> findByNameWithAllHamster(@Param("namepart") String namePart);

    @Query("select host from Host host where host.name like concat('%',:namepart,'%')")
    List<Host> findByNameWithoutHamster(@Param("namepart") String namePart);

    @Query("select  host from Host host left join fetch host.hamsters where host.address like concat('%',:city,'%') ")
    List<Host> findByCityWithHamster(@Param("city") String city);

    @Query("select host from Host host left join fetch host.hamsters where host.id = :id")
    Host findByIdWithAllHamster(@Param("id") long id);



}