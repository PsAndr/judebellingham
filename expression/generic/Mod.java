package expression.generic;

public class Mod<T extends BaseNumber<T>> extends BinaryExpression<T> {
    public Mod(final AllExpression<T> left, final AllExpression<T> right) {
        super(left, right);
    }

    @Override
    protected String getSign() {
        return "mod";
    }

    @Override
    protected T getOperationResult(final T a, final T b) {
        return a.mod(b);
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
