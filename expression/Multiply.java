package expression;

import java.util.Map;

public class Multiply extends BinaryExpression {
    public Multiply(final MaxExpression left, final MaxExpression right) {
        super(left, right);
    }

    @Override
    protected String getSign() {
        return "*";
    }

    @Override
    public int evaluate(int x) {
        return left.evaluate(x) * right.evaluate(x);
    }

    @Override
    public int getPriority() {
        return 2;
    }

    @Override
    public boolean isFlag() {
        return true;
    }

    @Override
    public float evaluateF(Map<String, Float> variables) {
        return left.evaluateF(variables) * right.evaluateF(variables);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return left.evaluate(x, y, z) * right.evaluate(x, y, z);
    }
}
