package hungarian_hamster_rescue.exceptions;

public class HostIsInactiveException extends RuntimeException{
    public HostIsInactiveException(long id) {
        super(String.format("The temporary host with the given ID (%d) currently cannot take a hamster.", id));
    }
}
