package expression.exceptions;

public class UnsupportedModeException extends OperationException {
    public UnsupportedModeException(String log) {
        super("Unsupported mode: " + log);
    }
}
