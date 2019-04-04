package expression.exceptions;

public abstract class OperationException extends Exception {
    public OperationException (String log) {
        super(log);
    }
}
