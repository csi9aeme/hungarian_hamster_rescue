package hungarian_hamster_resque.repositories;

import hungarian_hamster_resque.models.Hamster;
import hungarian_hamster_resque.models.Host;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HostRepository extends JpaRepository<Host, Long> {

    @Query("select host from Host host left join fetch host.hamsters where host.name like concat('%',:name,'%')")
    List<Host> findByNameWithAllHamster(@Param("name") String name);

    @Query("select host from Host host left join fetch host.hamsters where host.hostStatus = 'ACTIVE'")
    List<Host> findOnlyActiveWithAllHamster();

    @Query("select host from Host host left join fetch host.hamsters where host.hostStatus = 'ACTIVE' and host.address like concat('%',:city,'%')")
    List<Host> findOnlyActiveWithAllHamsterByCity(@Param("city") String city);

    @Query("select host from Host host where host.name like concat('%',:namepart,'%')")
    List<Host> findByNameWithoutHamster(@Param("namepart") String namePart);

    @Query("select  host from Host host left join fetch host.hamsters where host.address like concat('%',:city,'%') ")
    List<Host> findByCityWithHamster(@Param("city") String city);

    @Query("select host from Host host left join fetch host.hamsters where host.id = :id")
    Host findByIdWithAllHamster(@Param("id") long id);



}