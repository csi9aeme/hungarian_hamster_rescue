package hungarian_hamster_resque.models;

import jakarta.persistence.*;


public class Pictures {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Hamster hamster;

    private String src;
}
