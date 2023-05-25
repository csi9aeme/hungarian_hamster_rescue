package hungarian_hamster_resque.exceptions;

public class HamsterStatusNotAcceptableException extends RuntimeException{

    public HamsterStatusNotAcceptableException(String status) {
        super(String.format("A megadott örökbefogadhatósági állapot (%s) nem megfelelő.", status));
    }
}
