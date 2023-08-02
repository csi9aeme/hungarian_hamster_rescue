package hungarian_hamster_resque.exceptions;

public class HamsterWithIdNotExistException extends RuntimeException {

    public HamsterWithIdNotExistException(long id) {
        super(String.format("The hamster with the given ID (%d) is not exist.", id));
    }
}
