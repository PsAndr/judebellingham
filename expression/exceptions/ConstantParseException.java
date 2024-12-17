package expression.exceptions;

public class ConstantParseException extends WrongExpressionException {
    public ConstantParseException(final String message, final int index) {
        super(message, index);
    }
}
