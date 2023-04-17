package hungarian_hamster_resque.exceptions;

public class AdoptiveCantDeleteBecauseHamstersListNotEmptyException extends RuntimeException {
    public AdoptiveCantDeleteBecauseHamstersListNotEmptyException(long id) {
            super(String.format("A megadott ID-val (%s) rendelkező örökbefogadó nem törölhető, mert" +
                    "tartozik hozzá már örökbeadott hörcsög.", id));
        }
}
