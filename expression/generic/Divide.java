package expression.generic;

public class Divide<T extends BaseNumber<T>> extends BinaryExpression<T> {
    public Divide(final AllExpression<T> left, final AllExpression<T> right) {
        super(left, right);
    }

    @Override
    protected String getSign() {
        return "/";
    }

    @Override
    protected T getOperationResult(final T a, final T b) {
        return a.divide(b);
    }

    @Override
    public int getPriority() {
        return 2;
    }

    @Override
    public boolean isPriorityOperation() {
        return true;
    }

    @Override
    public boolean isChangeOperation() {
        return true;
    }
}
