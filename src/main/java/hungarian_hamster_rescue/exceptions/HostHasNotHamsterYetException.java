package hungarian_hamster_rescue.exceptions;

public class HostHasNotHamsterYetException extends RuntimeException {

    public HostHasNotHamsterYetException(long id) {
            super(String.format("The temporary host with the requested ID (%d) does not currently have a hamster.", id));
        }
}
