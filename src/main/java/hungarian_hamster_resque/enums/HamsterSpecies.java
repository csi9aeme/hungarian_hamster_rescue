package hungarian_hamster_resque.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum HamsterSpecies {
    GOLDEN("golden"),
    HYBRID_DWARF("hybrid djungarian dwarf"),
    DWARF("djungarian dwarf"),
    CAMPBELL("campbell's dwarf"),
    ROBOROVSKI("roborovski dwarf"),
    CHINESE("chinese");

    private String nameOfSpecies;

    public String getNameOfSpecies() {
        return nameOfSpecies;
    }
}

