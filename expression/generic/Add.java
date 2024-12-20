package expression.generic;

public class Add<T extends BaseNumber<T>> extends BinaryExpression<T> {
    public Add(final AllExpression<T> left, final AllExpression<T> right) {
        super(left, right);
    }

    @Override
    protected String getSign() {
        return "+";
    }

    @Override
    protected T getOperationResult(final T a, final T b) {
        return a.add(b);
    }

    @Override
    public int getPriority() {
        return 1;
    }
}
