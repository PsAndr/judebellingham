package expression;

import java.util.Map;

public class Negative extends UnaryExpression {
    public Negative(final AllExpression expression) {
        super(expression);
    }

    @Override
    protected boolean isRight() {
        return false;
    }

    @Override
    protected String getSign() {
        return "-";
    }

    @Override
    public int evaluate(final int x) {
        return -expression.evaluate(x);
    }

    @Override
    public float evaluateF(final Map<String, Float> variables) {
        return -expression.evaluateF(variables);
    }

    @Override
    public int evaluate(final int x, final int y, final int z) {
        return -expression.evaluate(x, y, z);
    }

    @Override
    public int getPriority() {
        return 10;
    }
}
