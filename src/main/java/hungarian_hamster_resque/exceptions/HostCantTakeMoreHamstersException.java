package hungarian_hamster_resque.exceptions;

public class HostCantTakeMoreHamstersException extends RuntimeException {
    public HostCantTakeMoreHamstersException(Long id) {
        super(String.format("The temporary host with the given ID (%d) not exist. Az ideiglenes befogadó a megadott ID-val (%d) nem tud több hörcsögöt fogadni.", id));
    }
}
