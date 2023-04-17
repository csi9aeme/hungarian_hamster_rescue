package hungarian_hamster_resque.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum HamsterStatus {

    ADOPTABLE("örökbefogadható"),
    ADOPTED ("örökbefogadott"),
    UNDER_MEDICAL_TREATMENT("kezelés alatt áll, de örökbefogadható"),
    PERMANENTLY_CARED_FOR("tartós gondozott, nem örökbefogadható"),
    DECEASED("elhunyt");

    @JsonValue
    private String status;

    @JsonValue
    public String getStatus() {
        return status;
    }
}
