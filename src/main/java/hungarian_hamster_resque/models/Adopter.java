package hungarian_hamster_resque.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "adopters")
public class Adopter {

    @Id
    @Column(name = "adopter_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Address address;

    @Embedded
    private Contacts contacts;

    @OneToMany(mappedBy = "adopter", cascade = {CascadeType.ALL})
    private List<Hamster> hamsters = new ArrayList<>();

    public Adopter(String name, Address address, Contacts contacts) {
        this.name = name;
        this.address = address;
        this.contacts = contacts;
    }
    public Adopter(String name, Address address, Contacts contacts, List<Hamster> hamsters) {
        this.name = name;
        this.address = address;
        this.contacts = contacts;
        this.hamsters = hamsters;
    }

    public void addHamster(Hamster hamster) {
        hamsters.add(hamster);
        hamster.setAdopter(this);
    }
}
