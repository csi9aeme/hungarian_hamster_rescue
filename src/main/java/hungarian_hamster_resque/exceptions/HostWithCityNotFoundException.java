package hungarian_hamster_resque.exceptions;

public class HostWithCityNotFoundException extends RuntimeException {
    public HostWithCityNotFoundException(String city) {
        super(String.format("There are currently no temporary hosts in the specified city: (%s).", city));

    }
}
