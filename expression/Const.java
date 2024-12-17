package expression;

import java.util.Map;

public class Const implements AllExpression {
    private final Number value;

    public Const(final int value) {
        this.value = value;
    }

    public Const(final float value) {
        this.value = value;
    }

    @Override
    public int evaluate(final int x) {
        return (int) value;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Const c) {
            return this.value.equals(c.value);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean isChangeOperation() {
        return true;
    }

    @Override
    public float evaluateF(final Map<String, Float> variables) {
        return (float) value;
    }

    @Override
    public int evaluate(final int x, final int y, final int z) {
        return evaluate(x);
    }
}
