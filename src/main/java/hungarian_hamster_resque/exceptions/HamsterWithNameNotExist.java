package hungarian_hamster_resque.exceptions;

public class HamsterWithNameNotExist extends RuntimeException {
    public HamsterWithNameNotExist(String hamsterName) {
        super(String.format("A keresett névrészlettel (%s) nincs hörcsög  az adatbázisban.", hamsterName));

    }
}
