package hungarian_hamster_resque.exceptions;

public class HostIsInactiveException extends RuntimeException{
    public HostIsInactiveException(long id) {
        super(String.format("The temporary host with the given ID (%d) currently cannot take a hamster.", id));
    }
}
