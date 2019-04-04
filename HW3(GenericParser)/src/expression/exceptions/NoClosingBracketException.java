package expression.exceptions;

public class NoClosingBracketException extends ParsingException {
    public NoClosingBracketException(String log) {
        super(log);
    }
}

