package hungarian_hamster_resque.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum HostStatus {

    ACTIVE("aktív"), INACTIVE("inaktív");

    private String hostStatus;

    @JsonValue
    public String getHostStatus() {
        return hostStatus;
    }
}
