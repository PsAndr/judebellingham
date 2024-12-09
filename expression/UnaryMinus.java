package expression;

import java.util.Map;

public class UnaryMinus implements SimpleExpression {
    public MaxExpression expression;

    public UnaryMinus(MaxExpression expression) {
        this.expression = expression;
    }

    @Override
    public int evaluate(int x) {
        return -expression.evaluate(x);
    }

    @Override
    public float evaluateF(Map<String, Float> variables) {
        return -expression.evaluateF(variables);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return -expression.evaluate(x, y, z);
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String toMiniString() {
        if (expression instanceof SimpleExpression) {
            return "- " + expression.toMiniString();
        }
        return "-(" + expression.toMiniString() + ")";
    }

    @Override
    public String toString() {
        return "-(" + expression + ")";
    }
}
