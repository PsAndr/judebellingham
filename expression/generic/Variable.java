package expression.generic;

import java.util.Map;

public class Variable<T extends BaseNumber<T>> implements AllExpression<T> {
    private final String name;

    public Variable(final String name) {
        this.name = name;
        if (name.isEmpty()) {
            throw new RuntimeException("Invalid variable name: " + name);
        }
    }

    public String getNameId() {
        return String.valueOf(name.charAt(name.length() - 1));
    }

    @Override
    public T evaluateNumber(Map<String, T> mp) {
        if (mp.containsKey(getNameId())) {
            return mp.get(getNameId());
        }
        throw new RuntimeException("Invalid variable name: " + getNameId());
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean isChangeOperation() {
        return true;
    }

    @Override
    public boolean isPriorityOperation() {
        return AllExpression.super.isPriorityOperation();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Variable<?> var) {
            return name.equals(var.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
