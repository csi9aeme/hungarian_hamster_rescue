package hungarian_hamster_resque.exceptions;


public class HamsterNotAdoptedYetException extends RuntimeException{

    public HamsterNotAdoptedYetException() {
        super(String.format("Not adopted yet."));
    }
}
