package hungarian_hamster_resque.exceptions;

public class HostHasNotHamsterYetException extends RuntimeException {

    public HostHasNotHamsterYetException(long id) {
            super(String.format("A keresett ID-val (%d) rendelkező ideiglenes befogadónak nincs jelenleg hörcsöge.", id));
        }
}
