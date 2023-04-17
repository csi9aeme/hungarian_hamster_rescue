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
@Table(name = "adoptives")
public class Adoptive {

    @Id
    @Column(name = "adoptive_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    @OneToMany(cascade = {CascadeType.PERSIST})
    private List<Hamster> hamsters = new ArrayList<>();

    public Adoptive(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public Adoptive(Long id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public void addHamster(Hamster hamster) {
        hamsters.add(hamster);
        hamster.setAdoptive(this);
    }
}
