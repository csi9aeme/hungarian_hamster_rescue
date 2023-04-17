package hungarian_hamster_resque.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum HamsterSpecies {
    GOLDEN("szíriai aranyhörcsög"),
    HYBRID_DWARF("hibrid dzsgunráiai törpehörcsög"),
    DWARF("dzsungáriai törpehörcsög"),
    CAMPBELL("campbell törpehörcsög"),
    ROBOROVSKI("roborovszki törpehörcsög"),
    CHINESE("kínai törpehörcsög");

    @JsonValue
    private String nameOfSpecies;

    @JsonValue
    public String getNameOfSpecies() {
        return nameOfSpecies;
    }
}

