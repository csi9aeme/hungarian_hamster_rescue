package hungarian_hamster_rescue.exceptions;

public class HostWithNamePartNotExistException extends RuntimeException{

    public HostWithNamePartNotExistException(String s) {
        super(String.format("The temporary host with the given name (%s) is not exit.", s));
    }
}
