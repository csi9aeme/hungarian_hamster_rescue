package hungarian_hamster_rescue.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum Gender {

    FEMALE("female"), MALE("male"), UNKNOWN_YET("unknown for now");

    private String gender;

    public String getGender() {
        return gender;
    }
}
