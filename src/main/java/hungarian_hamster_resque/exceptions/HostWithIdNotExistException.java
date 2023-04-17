package hungarian_hamster_resque.exceptions;

public class HostWithIdNotExistException extends RuntimeException{

    public HostWithIdNotExistException(long id) {
        super(String.format("A keresett ID-val (%d) ideiglenes befogadó nincs az adatbázisban.", id));
    }

}
