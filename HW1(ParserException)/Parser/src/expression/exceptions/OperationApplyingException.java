package expression.exceptions;

public abstract class OperationApplyingException extends Exception {
    OperationApplyingException(String log) {
        super("\nOperationApplyingException: " + log);
    }
}
