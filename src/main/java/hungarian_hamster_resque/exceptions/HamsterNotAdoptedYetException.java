package hungarian_hamster_resque.exceptions;

import hungarian_hamster_resque.models.Hamster;

public class HamsterNotAdoptedYetException extends RuntimeException{

    public HamsterNotAdoptedYetException() {
        super(String.format("Not adopted yet."));
    }
}
