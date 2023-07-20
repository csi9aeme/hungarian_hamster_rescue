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
    GOLDEN("szíriai aranyhörcsög"),
    HYBRID_DWARF("hibrid dzsungáriai törpehörcsög"),
    DWARF("dzsungáriai törpehörcsög"),
    CAMPBELL("campbell törpehörcsög"),
    ROBOROVSKI("roborovszki törpehörcsög"),
    CHINESE("kínai csíkos törpehörcsög");

    private String nameOfSpecies;

    public String getNameOfSpecies() {
        return nameOfSpecies;
    }
}

