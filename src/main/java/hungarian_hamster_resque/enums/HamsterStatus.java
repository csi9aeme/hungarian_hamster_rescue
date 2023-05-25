package hungarian_hamster_resque.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum HamsterStatus {

    ADOPTABLE("örökbefogadható"),
    ADOPTED ("örökbefogadott"),
    UNDER_MEDICAL_TREATMENT("kezelés alatt áll, de örökbefogadható"),
    PERMANENTLY_CARED_FOR("tartós gondozott, nem örökbefogadható"),
    DECEASED("elhunyt");

    private String status;

    public String getStatus() {
        return status;
    }
}
