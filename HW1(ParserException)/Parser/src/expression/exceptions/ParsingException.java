package expression.exceptions;

public abstract class ParsingException extends Exception{
    ParsingException (String log, String cursor) {
        super("\nParsingException: " + log + " for " + cursor);
    }
}
