package expression.exceptions;

public class DivisionByZeroException extends OperationException {
    public DivisionByZeroException() {
        super("DivisionByZero");
    }
}
