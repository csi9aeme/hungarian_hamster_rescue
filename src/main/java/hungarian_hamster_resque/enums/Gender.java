package hungarian_hamster_resque.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum Gender {

    FEMALE("nőstény"), MALE("hím"), UNKNOWN_YET("egyelőre ismeretlen");

    private String gender;

    public String getGender() {
        return gender;
    }
}
