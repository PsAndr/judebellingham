package expression;

import java.util.Map;

public class Subtract extends BinaryExpression {
    public Subtract(final MaxExpression left, final MaxExpression right) {
        super(left, right);
    }

    @Override
    protected String getSign() {
        return "-";
    }

    @Override
    public int evaluate(int x) {
        return left.evaluate(x) - right.evaluate(x);
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public float evaluateF(Map<String, Float> variables) {
        return left.evaluateF(variables) - right.evaluateF(variables);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return left.evaluate(x, y, z) - right.evaluate(x, y, z);
    }
}
