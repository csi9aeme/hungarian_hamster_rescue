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
    GOLDEN("golden hamster"),
    HYBRID_DWARF("hybrid djungarian dwarf hamster"),
    DWARF("djungarian dwarf hamster"),
    CAMPBELL("campbell's dwarf hamster"),
    ROBOROVSKI("roborovski dwarf hamster"),
    CHINESE("chinese hamster");

    private String nameOfSpecies;

    public String getNameOfSpecies() {
        return nameOfSpecies;
    }
}

