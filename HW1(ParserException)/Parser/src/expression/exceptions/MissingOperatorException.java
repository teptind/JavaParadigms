package expression.exceptions;

public class MissingOperatorException extends ParsingException {
    public MissingOperatorException(String cursor) {
        super("MissedOperator", cursor);
    }
}
