package expression.exceptions;

public class UnidentifiedSymbolException extends ParsingException{
    public UnidentifiedSymbolException(String cursor) {
        super("UnidentifiedSymbol", cursor);
    }
}
