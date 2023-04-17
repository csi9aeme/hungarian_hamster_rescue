package hungarian_hamster_resque.exceptions;

public class AdoptiveWithNameNotExistException extends RuntimeException{
    public AdoptiveWithNameNotExistException(String s) {
        super(String.format("A keresett névrészlettel (%s) örökbefogadó nincs az adatbázisban.", s));
    }
}
