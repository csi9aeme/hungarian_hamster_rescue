package hungarian_hamster_resque.exceptions;

public class HostWithNamePartNotExistException extends RuntimeException{

    public HostWithNamePartNotExistException(String s) {
        super(String.format("A keresett névrészlettel (%s) ideiglenes befogadó nincs az adatbázisban.", s));
    }
}
