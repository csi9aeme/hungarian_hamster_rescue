package hungarian_hamster_rescue.exceptions;

public class AdopterWithCityNotExistException extends RuntimeException{

    public AdopterWithCityNotExistException(String s) {
        super(String.format("There are currently no adopters in the specified city: (%s).", s));
    }
}
