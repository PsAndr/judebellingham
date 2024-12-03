package expression;

import java.util.Map;

public class Const implements MaxExpression {
    private final int value;
    private final float valueF;

    private final boolean real;

    public Const(int value) {
        this.value = value;
        this.valueF = 0;
        real = false;
    }

    public Const(float value) {
        this.valueF = value;
        this.value = 0;
        real = true;
    }

    @Override
    public int evaluate(int x) {
        if (real) {
            return Math.round(valueF);
        }
        return value;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String toString() {
        if (real) {
            return String.valueOf(valueF);
        }
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object obj) {
        throw new RuntimeException();
        /*if (obj instanceof Const c) {
            return this.value == c.value;
        }
        return false;*/
    }

    @Override
    public int hashCode() {
        if (real) {
            return Math.round(valueF);
        }
        return value;
    }

    @Override
    public boolean isFlag() {
        return true;
    }

    @Override
    public float evaluateF(Map<String, Float> variables) {
        return this.valueF;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return evaluate(x);
    }
}
