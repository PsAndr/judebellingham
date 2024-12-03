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
    protected int getOperationResult(int a, int b) {
        return a - b;
    }

    @Override
    protected float getOperationResult(float a, float b) {
        return a - b;
    }

    @Override
    public int getPriority() {
        return 1;
    }
}
