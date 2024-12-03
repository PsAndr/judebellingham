package expression;

import java.util.Map;

public class Variable implements MaxExpression {
    private final String name;

    public Variable(final String name) {
        this.name = name;
    }

    @Override
    public int evaluate(final int x) {
        return x;
    }

    @Override
    public int evaluate(final int x, final int y, final int z) {
        return switch (name) {
            case "x" -> x;
            case "y" -> y;
            case "z" -> z;
            default -> throw new RuntimeException("Invalid variable name: " + name);
        };
    }

    @Override
    public float evaluateF(final Map<String, Float> variables) {
        if (variables.containsKey(name)) {
            return variables.get(name);
        }
        throw new RuntimeException("Variable " + name + " not found");
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
    public boolean equals(Object obj) {
        if (obj instanceof Variable var) {
            return name.equals(var.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
