package hungarian_hamster_rescue.enums;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum HostStatus {

    ACTIVE("active"), INACTIVE("inactive");

    private String hostStatus;

    public String getHostStatus() {
        return hostStatus;
    }
}
