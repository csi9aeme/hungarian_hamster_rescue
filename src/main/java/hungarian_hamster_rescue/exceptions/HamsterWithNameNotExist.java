package hungarian_hamster_rescue.exceptions;

public class HamsterWithNameNotExist extends RuntimeException {
    public HamsterWithNameNotExist(String hamsterName) {
        super(String.format("The hamster with the given name (%s) is not exit.", hamsterName));

    }
}
