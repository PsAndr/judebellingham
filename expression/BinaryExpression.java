package expression;

import java.util.Map;
import java.util.Objects;

public abstract class BinaryExpression implements AllExpression {
    protected final AllExpression left;
    protected final AllExpression right;

    public BinaryExpression(final AllExpression left, final AllExpression right) {
        this.left = left;
        this.right = right;
    }

    protected abstract String getSign();
    protected abstract int getOperationResult(final int a, final int b);
    protected abstract float getOperationResult(final float a, final float b);

    @Override
    public int evaluate(final int x) {
        return getOperationResult(left.evaluate(x), right.evaluate(x));
    }

    @Override
    public float evaluateF(final Map<String, Float> variables) {
        return getOperationResult(left.evaluateF(variables), right.evaluateF(variables));
    }

    @Override
    public int evaluate(final int x, final int y, final int z) {
        return getOperationResult(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }

    @Override
    public String toString() {
        return "(" + left.toString() + " " + getSign() + " " + right.toString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof BinaryExpression other) {
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
                && (!isChangeOperation() || right.isPriorityOperation()));
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
