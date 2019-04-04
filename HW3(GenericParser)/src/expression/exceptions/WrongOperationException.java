package expression.exceptions;

public class WrongOperationException extends ParsingException {
    public WrongOperationException(String log) {
        super(log);
    }
}
