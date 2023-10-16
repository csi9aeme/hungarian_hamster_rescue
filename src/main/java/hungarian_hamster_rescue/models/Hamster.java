package hungarian_hamster_rescue.models;

import hungarian_hamster_rescue.enums.Gender;
import hungarian_hamster_rescue.enums.HamsterStatus;
import hungarian_hamster_rescue.enums.HamsterSpecies;
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

        private String color;

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

        @OneToMany(mappedBy = "hamster")
        private List<Picture>  pictures;

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

        public Hamster(Long id, String name, HamsterSpecies hamsterSpecies, String color, Gender gender, LocalDate dateOfBirth, HamsterStatus hamsterStatus, LocalDate startOfFostering, Host host, String description, List<WeeklyReport> weeklyReports, List<Picture> pictures) {
                this.id = id;
                this.name = name;
                this.hamsterSpecies = hamsterSpecies;
                this.color = color;
                this.gender = gender;
                this.dateOfBirth = dateOfBirth;
                this.hamsterStatus = hamsterStatus;
                this.startOfFostering = startOfFostering;
                this.host = host;
                this.description = description;
                this.weeklyReports = weeklyReports;
                this.pictures = pictures;
        }
}

