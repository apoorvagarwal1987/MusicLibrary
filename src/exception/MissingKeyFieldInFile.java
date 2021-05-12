package exception;

public class MissingKeyFieldInFile extends RuntimeException {
    public MissingKeyFieldInFile(String errorMessage) {
        super(errorMessage);
    }
}
