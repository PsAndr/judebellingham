package expression.exceptions;

public class BracketParseException extends WrongExpressionException {
    public BracketParseException(final String message, final int index) {
        super(message, index);
    }
}
