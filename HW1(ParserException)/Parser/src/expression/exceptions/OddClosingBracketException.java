package expression.exceptions;

public class OddClosingBracketException extends ParsingException{
    public OddClosingBracketException(String cursor) {
        super("OddClosingBracket", cursor);
    }
}
