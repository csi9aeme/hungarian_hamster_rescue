package hungarian_hamster_resque.exceptions;

public class HostIsInactiveException extends RuntimeException{
    public HostIsInactiveException(long id) {
        super(String.format("A megadott ID-val (%d) rendelkező ideiglenes befogadó jelenleg nem tud hörcsögöt fogadni.", id));
    }
}
