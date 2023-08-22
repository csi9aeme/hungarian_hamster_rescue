package hungarian_hamster_resque.models;

import hungarian_hamster_resque.enums.HostStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "hosts")
public class Host {

    @Id
    @Column(name = "host_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    @Column(name = "host_status")
    @Enumerated(EnumType.STRING)
    private HostStatus hostStatus;

    @Column(name = "holding_capacity")
    private int capacity;

    @OneToMany(mappedBy = "host", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.REMOVE} )
    private List<Hamster> hamsters = new ArrayList<>();

    @OneToMany(mappedBy = "host", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.REMOVE})
    private List<WeeklyReport> weeklyReports;

    public Host(String name, String address, int capacity) {
        this.name = name;
        this.address = address;
        this.capacity = capacity;
    }

    public Host(String name, String address, int capacity, List<Hamster> hamsters) {
        this.name = name;
        this.address = address;
        this.capacity = capacity;
        this.hamsters = hamsters;
    }

    public Host(Long id, String name, String address, HostStatus hostStatus, int capacity) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.capacity = capacity;
        this.hostStatus = hostStatus;
    }

    public Host(String name, String address, HostStatus hostStatus, int capacity, List<Hamster> hamsters) {
        this.name = name;
        this.address = address;
        this.hostStatus = hostStatus;
        this.capacity = capacity;
        this.hamsters = hamsters;
    }

    public Host(String name, String address, HostStatus hostStatus, int capacity) {
        this.name = name;
        this.address = address;
        this.hostStatus = hostStatus;
        this.capacity = capacity;
    }
    public Host(Long id, String name, String address, HostStatus hostStatus, int capacity, List<Hamster> hamsters) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.capacity = capacity;
        this.hostStatus = hostStatus;
        this.hamsters = hamsters;
    }

    public void addHamster(Hamster hamster) {
        hamsters.add(hamster);
        hamster.setHost(this);
    }

}
