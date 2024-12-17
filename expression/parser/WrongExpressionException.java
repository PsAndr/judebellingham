package expression.parser;

public class WrongExpressionException extends RuntimeException {
    public WrongExpressionException(String message) {
        super(message);
    }

    public WrongExpressionException() {
        super();
    }
}
