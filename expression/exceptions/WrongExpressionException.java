package expression.exceptions;

public class WrongExpressionException extends RuntimeException {
    public WrongExpressionException(final String message, final int index) {
        super(message + " (index: " + index + ")");
    }
}

