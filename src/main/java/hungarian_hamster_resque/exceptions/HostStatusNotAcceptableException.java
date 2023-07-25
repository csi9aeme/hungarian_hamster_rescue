package hungarian_hamster_resque.exceptions;

public class HostStatusNotAcceptableException extends RuntimeException{

    public HostStatusNotAcceptableException(String status) {
        super(String.format("A megadott állapot (%s) nem megfelelő.", status));
    }
}
