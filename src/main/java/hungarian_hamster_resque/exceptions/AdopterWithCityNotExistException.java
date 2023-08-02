package hungarian_hamster_resque.exceptions;

public class AdopterWithCityNotExistException extends RuntimeException{

    public AdopterWithCityNotExistException(String s) {
        super(String.format("A keresett városban (%s) jelenleg nincs örökbefogadó.", s));
    }
}
