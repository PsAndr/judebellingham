package expression.exceptions;

public class TokenException extends WrongExpressionException {
    public TokenException(final String message, final int index) {
        super(message, index);
    }
}
