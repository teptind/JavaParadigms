package expression.exceptions;

public class OverflowException extends OperationApplyingException{
    public OverflowException() {
        super("Overflow");
    }
}
