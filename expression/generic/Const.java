package expression.generic;

import java.util.Map;

public class Const<T extends BaseNumber<T>> implements AllExpression<T> {
    private final T value;

    public Const(final T value) {
        this.value = value;
    }

    @Override
    public T evaluateNumber(Map<String, T> mp) {
        return value;
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
        if (obj instanceof Const<?> c) {
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
}
