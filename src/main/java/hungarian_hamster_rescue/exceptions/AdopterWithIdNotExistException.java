package hungarian_hamster_rescue.exceptions;

public class AdopterWithIdNotExistException extends RuntimeException{

    public AdopterWithIdNotExistException(long id) {
        super(String.format("The adopter with the given ID (%s) not exist.", id));
    }

}
