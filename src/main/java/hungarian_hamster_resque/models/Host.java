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

    @Embedded
    private Address address;

    @Embedded
    private Contacts contacts;

    @Column(name = "host_status")
    @Enumerated(EnumType.STRING)
    private HostStatus hostStatus;

    @Column(name = "holding_capacity")
    private int capacity;

    @OneToMany(mappedBy = "host", cascade = CascadeType.ALL )
    private List<Hamster> hamsters = new ArrayList<>();

    @OneToMany(mappedBy = "host", cascade = CascadeType.ALL)
    private List<WeeklyReport> weeklyReports;


    public Host(String name, Address address, Contacts contacts, HostStatus hostStatus, int capacity, List<Hamster> hamsters, List<WeeklyReport> weeklyReports) {
        this.name = name;
        this.address = address;
        this.contacts = contacts;
        this.hostStatus = hostStatus;
        this.capacity = capacity;
        this.hamsters = hamsters;
        this.weeklyReports = weeklyReports;
    }

    public void addHamster(Hamster hamster) {
        hamsters.add(hamster);
        hamster.setHost(this);
    }




}
