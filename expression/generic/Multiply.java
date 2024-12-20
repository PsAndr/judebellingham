package expression.generic;

public class Multiply<T extends BaseNumber<T>> extends BinaryExpression<T> {
    public Multiply(final AllExpression<T> left, final AllExpression<T> right) {
        super(left, right);
    }

    @Override
    protected String getSign() {
        return "*";
    }
    @Override
    protected T getOperationResult(final T a, final T b) {
        return a.multiply(b);
    }

    @Override
    public int getPriority() {
        return 2;
    }
}
