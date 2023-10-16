package hungarian_hamster_rescue.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum HamsterStatus {

    ADOPTABLE("adoptable"),
    ADOPTED ("adopted"),
    UNDER_MEDICAL_TREATMENT("under medical treatment"),
    PERMANENTLY_CARED_FOR("permanently cared for"),
    DECEASED("deceased");

    private String status;

    public String getStatus() {
        return status;
    }
}
