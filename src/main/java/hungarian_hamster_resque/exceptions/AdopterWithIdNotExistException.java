package hungarian_hamster_resque.exceptions;

public class AdopterWithIdNotExistException extends RuntimeException{

    public AdopterWithIdNotExistException(long id) {
        super(String.format("A keresett ID-val (%d) örökbefogadó nincs az adatbázisban.", id));
    }

}
