package expression.exceptions;

public abstract class ParsingException extends Exception {
    public ParsingException (String log) {
        super(log);
    }
}
