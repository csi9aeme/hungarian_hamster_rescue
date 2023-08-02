package hungarian_hamster_resque.exceptions;

public class AdopterWithNameNotExistException extends RuntimeException{
    public AdopterWithNameNotExistException(String s) {
        super(String.format("A keresett névrészlettel (%s) örökbefogadó nincs az adatbázisban.", s));
    }
}
