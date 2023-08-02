package hungarian_hamster_resque.exceptions;

public class AdopterCantDeleteBecauseHamstersListNotEmptyException extends RuntimeException {
    public AdopterCantDeleteBecauseHamstersListNotEmptyException(long id) {
            super(String.format("The adopter with the given ID (%s) cannot be deleted because it already " +
                    "has an adopted hamster.", id));
        }
}
