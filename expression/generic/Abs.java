package expression.generic;

public class Abs<T extends BaseNumber<T>> extends UnaryExpression<T> {
    public Abs(final AllExpression<T> expression) {
        super(expression);
    }

    @Override
    protected boolean isRight() {
        return false;
    }

    @Override
    protected String getSign() {
        return "abs";
    }

    @Override
    protected T getOperationResult(final T val) {
        if (val.compareTo(val.negate()) < 0) {
            return val.negate();
        }
        return val;
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
