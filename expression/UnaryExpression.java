package expression;

import java.util.Objects;

public abstract class UnaryExpression implements AllExpression {
    protected final AllExpression expression;

    public UnaryExpression(final AllExpression expression) {
        this.expression = expression;
    }

    protected abstract boolean isRight();
    protected abstract String getSign();

    @Override
    public String toMiniString() {
        boolean flag = expression.getPriority() < getPriority();
        String ans;
        if (flag) {
            ans = "(" + expression.toMiniString() + ")";
        } else {
            ans = expression.toMiniString();
        }
        if (isRight()) {
            ans += getSign();
        } else {
            ans = getSign() + (flag ? "" : " ") + ans;
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
        if (obj instanceof UnaryExpression other) {
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
