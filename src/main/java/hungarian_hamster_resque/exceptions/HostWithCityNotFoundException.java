package hungarian_hamster_resque.exceptions;

public class HostWithCityNotFoundException extends RuntimeException {
    public HostWithCityNotFoundException(String city) {
        super(String.format("A keresett településen (%s) ideiglenes befogadó nem található.", city));

    }
}
