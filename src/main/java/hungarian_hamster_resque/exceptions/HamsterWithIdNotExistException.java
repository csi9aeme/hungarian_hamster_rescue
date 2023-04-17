package hungarian_hamster_resque.exceptions;

public class HamsterWithIdNotExistException extends RuntimeException {

    public HamsterWithIdNotExistException(long id) {
        super(String.format("A keresett ID-val (%d) hörcsög nincs az adatbázisban.", id));
    }
}
