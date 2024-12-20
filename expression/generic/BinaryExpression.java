package expression.generic;

import java.util.Map;
import java.util.Objects;

public abstract class BinaryExpression<T extends BaseNumber<T>> implements AllExpression<T> {
    protected final AllExpression<T> left;
    protected final AllExpression<T> right;

    public BinaryExpression(final AllExpression<T> left, final AllExpression<T> right) {
        this.left = left;
        this.right = right;
    }

    protected abstract String getSign();
    protected T getOperationResult(final T a, final T b) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected boolean isAssociative() {
        return !isChangeOperation();
    }

    @Override
    public T evaluateNumber(final Map<String, T> mp) {
        return getOperationResult(left.evaluateNumber(mp), right.evaluateNumber(mp));
    }

    @Override
    public String toString() {
        return "(" + left.toString() + " " + getSign() + " " + right.toString() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof BinaryExpression<?> other) {
            return left.equals(other.left) && right.equals(other.right)
                    && Objects.equals(getSign(), other.getSign());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right, getSign());
    }

    @Override
    public String toMiniString() {
        StringBuilder sb = new StringBuilder();
        boolean flag = left.getPriority() < getPriority();
        if (flag) {
            sb.append("(");
        }
        sb.append(left.toMiniString());
        if (flag) {
            sb.append(")");
        }
        sb.append(" ").append(getSign()).append(" ");
        flag = right.getPriority() < getPriority() || (right.getPriority() == getPriority()
                && (isChangeOperation() || right.isPriorityOperation())
                && (!isAssociative() || getClass() != right.getClass()));
        if (flag) {
            sb.append("(");
        }
        sb.append(right.toMiniString());
        if (flag) {
            sb.append(")");
        }
        return sb.toString();
    }
}
