package hungarian_hamster_resque.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;



@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Gender {

    FEMALE("nőstény"), MALE("hím"), UNKNOWN_YET("egyelőre ismeretlen");

    @JsonValue
    private String gender;

    @JsonValue
    public String getGender() {
        return gender;
    }
}
