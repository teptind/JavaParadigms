package expression.exceptions;

public class NotEnoughArgumentsException extends ParsingException {
    public NotEnoughArgumentsException(String log) {
        super(log);
    }
}
