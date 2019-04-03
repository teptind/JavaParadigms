package expression.exceptions;

public class DivisionByZeroException extends OperationApplyingException{
    public DivisionByZeroException() {
        super("DivisionByZero");
    }
}
