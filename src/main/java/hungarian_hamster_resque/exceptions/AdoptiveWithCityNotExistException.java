package hungarian_hamster_resque.exceptions;

public class AdoptiveWithCityNotExistException extends RuntimeException{

    public AdoptiveWithCityNotExistException(String s) {
        super(String.format("A keresett városban (%s) jelenleg nincs örökbefogadó.", s));
    }
}
