package expression.exceptions;

public class ImpossibleToGetResultException extends ParsingException {
    public ImpossibleToGetResultException(String cursor) {
        super("ImpossibleToGetResult", cursor);
    }
}
