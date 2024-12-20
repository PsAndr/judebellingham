package expression.generic;

public class Negative<T extends BaseNumber<T>> extends UnaryExpression<T> {
    public Negative(final AllExpression<T> expression) {
        super(expression);
    }

    @Override
    protected boolean isRight() {
        return false;
    }

    @Override
    protected String getSign() {
        return "-";
    }

    @Override
    protected T getOperationResult(final T val) {
        return val.negate();
    }

    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    protected boolean canNearPlaceConst() {
        return false;
    }
}
