package hungarian_hamster_rescue.exceptions;

public class HamsterStatusNotAcceptableException extends RuntimeException{

    public HamsterStatusNotAcceptableException(String status) {
        super(String.format("The given status (%s) is not acceptable.", status));
    }
}
