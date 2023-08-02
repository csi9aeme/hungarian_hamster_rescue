package hungarian_hamster_resque.exceptions;

public class HamsterSpeciesNotExistException extends RuntimeException {
    public HamsterSpeciesNotExistException(String species) {
        super(String.format("The given species (%s) is wrong.", species));
    }
}
