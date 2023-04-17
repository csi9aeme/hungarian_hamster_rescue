package hungarian_hamster_resque.exceptions;

public class AdoptiveWithIdNotExistException extends RuntimeException{

    public AdoptiveWithIdNotExistException(long id) {
        super(String.format("A keresett ID-val (%d) örökbefogadó nincs az adatbázisban.", id));
    }

}
