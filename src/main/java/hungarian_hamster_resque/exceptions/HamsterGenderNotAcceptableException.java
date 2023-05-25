package hungarian_hamster_resque.exceptions;

public class HamsterGenderNotAcceptableException extends RuntimeException{

    public HamsterGenderNotAcceptableException(String gender) {
        super(String.format("Nem megfelelő nem."));
    }
}
