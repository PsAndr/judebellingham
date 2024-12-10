package expression;

import java.util.Map;

public class Const implements MaxExpression {
    private final double value;

    private final boolean real;

    public Const(final int value) {
        this.value = value;
        real = false;
    }

    public Const(final float value) {
        this.value = value;
        real = true;
    }

    @Override
    public int evaluate(final int x) {
        return Math.toIntExact(Math.round(value));
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String toString() {
        if (real) {
            return String.valueOf(Double.valueOf(value).floatValue());
        }
        return String.valueOf(Math.round(value));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Const c) {
            return this.value == c.value;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Math.toIntExact(Math.round(value) % Integer.MAX_VALUE);
    }

    @Override
    public boolean isChangeOperation() {
        return true;
    }

    @Override
    public float evaluateF(final Map<String, Float> variables) {
        return Double.valueOf(value).floatValue();
    }

    @Override
    public int evaluate(final int x, final int y, final int z) {
        return evaluate(x);
    }
}
