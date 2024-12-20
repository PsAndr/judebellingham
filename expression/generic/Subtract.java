package expression.generic;

public class Subtract<T extends BaseNumber<T>> extends BinaryExpression<T> {
    public Subtract(final AllExpression<T> left, final AllExpression<T> right) {
        super(left, right);
    }

    @Override
    protected String getSign() {
        return "-";
    }

    @Override
    protected T getOperationResult(final T a, final T b) {
        return a.sub(b);
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public boolean isChangeOperation() {
        return true;
    }
}
