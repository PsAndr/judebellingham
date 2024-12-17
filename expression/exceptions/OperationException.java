package expression.exceptions;

public class OperationException extends WrongExpressionException {
    public OperationException(final String message, final int index) {
        super(message, index);
    }
}
