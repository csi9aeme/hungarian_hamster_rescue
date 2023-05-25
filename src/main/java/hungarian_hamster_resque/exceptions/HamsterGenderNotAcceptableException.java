package hungarian_hamster_resque.exceptions;

public class HamsterGenderNotAcceptableException extends RuntimeException{

    public HamsterGenderNotAcceptableException(String gender) {
        super(String.format("A megadott nem (%s) nem megfelelő.", gender));
    }
}
