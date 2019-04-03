package expression.exceptions;

public class NoClosingBracketException extends ParsingException {
    public NoClosingBracketException(String cursor) {
        super("NoClosingBracket", cursor);
    }
}

