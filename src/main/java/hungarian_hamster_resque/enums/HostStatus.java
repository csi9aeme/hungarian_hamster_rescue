package hungarian_hamster_resque.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum HostStatus {

    ACTIVE("aktív"), INACTIVE("inaktív");

    private String hostStatus;

    public String getHostStatus() {
        return hostStatus;
    }
}
