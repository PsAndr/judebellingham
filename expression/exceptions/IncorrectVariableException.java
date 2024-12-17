package expression.exceptions;

public class IncorrectVariableException extends WrongExpressionException {
    public IncorrectVariableException(final String message, final int index) {
        super(message, index);
    }
}
