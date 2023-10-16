package hungarian_hamster_rescue.exceptions;

public class HostWithIdNotExistException extends RuntimeException{

    public HostWithIdNotExistException(long id) {
        super(String.format("The temporary host with the given ID (%d) is not exist.", id));
    }

}
