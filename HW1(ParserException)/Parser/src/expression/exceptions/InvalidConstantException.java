package expression.exceptions;

public class InvalidConstantException extends ParsingException {
    public InvalidConstantException(String cursor) {
        super("InvalidConstant", cursor);
    }
}
