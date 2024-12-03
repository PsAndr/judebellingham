package expression;

import java.util.Map;
import java.util.Objects;

public abstract class BinaryExpression implements MaxExpression {
    protected final MaxExpression left;
    protected final MaxExpression right;

    public BinaryExpression(final MaxExpression left, final MaxExpression right) {
        this.left = left;
        this.right = right;
    }

    protected abstract String getSign();

    @Override
    public String toString() {
        return "(" + left.toString() + " " + getSign() + " " + right.toString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        throw new RuntimeException();
        /*if (o instanceof BinaryExpression other) {
            return left.equals(other.left) && right.equals(other.right)
                    && Objects.equals(getSign(), other.getSign());
        }
        return false;*/
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
                && (!isFlag() || right.isFlag2()));
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
