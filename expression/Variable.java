package expression;

import java.util.Map;

public class Variable implements SimpleExpression, SimpleRightExpression {
    private final String name;

    public Variable(final String name) {
        this.name = name;
        if (name.isEmpty()) {
            throw new RuntimeException("Invalid variable name: " + name);
        }
    }

    @Override
    public int evaluate(final int x) {
        return x;
    }

    public String getNameId() {
        return String.valueOf(name.charAt(name.length() - 1));
    }

    @Override
    public int evaluate(final int x, final int y, final int z) {
        return switch (getNameId()) {
            case "x" -> x;
            case "y" -> y;
            case "z" -> z;
            default -> throw new RuntimeException("Invalid variable name: " + name);
        };
    }

    @Override
    public float evaluateF(final Map<String, Float> variables) {
        String id = getNameId();
        if (variables.containsKey(id)) {
            return variables.get(id);
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
