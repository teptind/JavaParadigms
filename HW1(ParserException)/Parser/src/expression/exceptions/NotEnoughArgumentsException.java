package expression.exceptions;

public class NotEnoughArgumentsException extends ParsingException {
    public NotEnoughArgumentsException(String cursor) {
        super("NotEnoughArguments", cursor);
    }
}
