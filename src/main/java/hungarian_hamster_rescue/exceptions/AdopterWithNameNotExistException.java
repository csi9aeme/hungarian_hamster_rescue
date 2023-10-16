package hungarian_hamster_rescue.exceptions;

public class AdopterWithNameNotExistException extends RuntimeException{
    public AdopterWithNameNotExistException(String s) {
        super(String.format("The adopter with the given name (%s) not exist.", s));
    }
}
