package hungarian_hamster_resque.models;

import hungarian_hamster_resque.enums.Gender;
import hungarian_hamster_resque.enums.HamsterStatus;
import hungarian_hamster_resque.enums.HamsterSpecies;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "hamsters")
public class Hamster {

        @Id
        @Column(name = "hamster_id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;

        @Enumerated(EnumType.STRING)
        @Column(name = "hamster_species")
        private HamsterSpecies hamsterSpecies;

        @Enumerated(EnumType.STRING)
        private Gender gender;

        @Column(name = "date_of_birth")
        private LocalDate dateOfBirth;

        @Enumerated(EnumType.STRING)
        @Column(name = "hamster_status")
        private HamsterStatus hamsterStatus;


        @Column(name ="start_of_fostering")
        private LocalDate startOfFostering;

        @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.REMOVE})
        private Host host;

        @ManyToOne(cascade = {CascadeType.PERSIST})
        private Adopter adopter;

        @Column(name = "date_of_adoption")
        private LocalDate dateOfAdoption;

        private String description;

        @OneToMany(mappedBy = "hamster", cascade = CascadeType.ALL)
        @Column(name = "weekly_reports")
        private List<WeeklyReport> weeklyReports;

        public Hamster(Long id, String name, HamsterSpecies hamsterSpecies, Gender gender, LocalDate dateOfBirth, HamsterStatus hamsterStatus, Host host, LocalDate startOfFostering) {
                this.id = id;
                this.name = name;
                this.hamsterSpecies = hamsterSpecies;
                this.gender = gender;
                this.dateOfBirth = dateOfBirth;
                this.hamsterStatus = hamsterStatus;
                this.host = host;
                this.startOfFostering = startOfFostering;
        }
        public Hamster(Long id, String name, HamsterSpecies hamsterSpecies, Gender gender, LocalDate dateOfBirth, HamsterStatus hamsterStatus, Host host, LocalDate startOfFostering, Adopter adopter, LocalDate dateOfAdoption, String description) {
                this.id = id;
                this.name = name;
                this.hamsterSpecies = hamsterSpecies;
                this.gender = gender;
                this.dateOfBirth = dateOfBirth;
                this.hamsterStatus = hamsterStatus;
                this.startOfFostering = startOfFostering;
                this.host = host;
                this.adopter = adopter;
                this.dateOfAdoption = dateOfAdoption;
                this.description = description;
        }

        public Hamster(String name, HamsterSpecies hamsterSpecies, Gender gender, LocalDate dateOfBirth, HamsterStatus hamsterStatus, Host host, LocalDate startOfFostering) {
                this.name = name;
                this.hamsterSpecies = hamsterSpecies;
                this.gender = gender;
                this.dateOfBirth = dateOfBirth;
                this.hamsterStatus = hamsterStatus;
                this.host = host;
                this.startOfFostering = startOfFostering;
        }

        public Hamster(String name, HamsterSpecies hamsterSpecies, Gender gender, LocalDate dateOfBirth, HamsterStatus hamsterStatus, LocalDate startOfFostering) {
                this.name = name;
                this.hamsterSpecies = hamsterSpecies;
                this.gender = gender;
                this.dateOfBirth = dateOfBirth;
                this.hamsterStatus = hamsterStatus;
                this.startOfFostering = startOfFostering;
        }

        public Hamster(String name, HamsterSpecies hamsterSpecies, Gender gender, LocalDate dateOfBirth, HamsterStatus hamsterStatus, LocalDate startOfFostering, Host host, String description) {
                this.name = name;
                this.hamsterSpecies = hamsterSpecies;
                this.gender = gender;
                this.dateOfBirth = dateOfBirth;
                this.hamsterStatus = hamsterStatus;
                this.startOfFostering = startOfFostering;
                this.host = host;
                this.description = description;
        }
}

