package expression.generic;

import java.util.Map;

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
    protected T getOperationResult(T val) {
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
