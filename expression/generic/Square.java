package expression.generic;

public class Square<T extends BaseNumber<T>> extends UnaryExpression<T> {
    public Square(final AllExpression<T> expression) {
        super(expression);
    }

    @Override
    protected boolean isRight() {
        return false;
    }

    @Override
    protected String getSign() {
        return "square";
    }

    @Override
    protected T getOperationResult(final T val) {
        return val.multiply(val);
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
