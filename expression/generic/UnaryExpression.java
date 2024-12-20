package expression.generic;

import java.util.Map;
import java.util.Objects;

public abstract class UnaryExpression<T extends BaseNumber<T>> implements AllExpression<T> {
    protected final AllExpression<T> expression;

    public UnaryExpression(final AllExpression<T> expression) {
        this.expression = expression;
    }

    protected abstract boolean isRight();
    protected boolean canNearPlaceConst() {
        return true;
    }

    protected abstract String getSign();

    protected abstract T getOperationResult(final T val);

    @Override
    public T evaluateNumber(Map<String, T> mp) {
        return getOperationResult(expression.evaluateNumber(mp));
    }

    @Override
    public String toMiniString() {
        boolean flag = expression.getPriority() < getPriority();
        String ans;
        if (flag) {
            ans = "(" + expression.toMiniString() + ")";
        } else {
            ans = expression.toMiniString();
        }
        String splitSigns = flag || canNearPlaceConst() ? "" : " ";
        if (isRight()) {
            ans += splitSigns + getSign();
        } else {
            ans = getSign() + splitSigns + ans;
        }
        return ans;
    }

    @Override
    public String toString() {
        String ans = "(" + expression + ")";
        if (isRight()) {
            ans += getSign();
        } else {
            ans = getSign() + ans;
        }
        return ans;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UnaryExpression<?> other) {
            return expression.equals(other.expression) && isRight() == other.isRight()
                    && getSign().equals(other.getSign());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(expression, isRight(), getSign());
    }
}
