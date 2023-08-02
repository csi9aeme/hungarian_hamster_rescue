package hungarian_hamster_resque.exceptions;

public class HostStatusNotAcceptableException extends RuntimeException{

    public HostStatusNotAcceptableException(String status) {
        super(String.format("The given status (%s) is not acceptable.", status));
    }
}
