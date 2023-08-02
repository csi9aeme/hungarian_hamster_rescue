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

    private String address;

    @OneToMany(mappedBy = "adopter", cascade = {CascadeType.PERSIST})
    private List<Hamster> hamsters = new ArrayList<>();

    public Adopter(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public Adopter(Long id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public void addHamster(Hamster hamster) {
        hamsters.add(hamster);
        hamster.setAdopter(this);
    }
}
