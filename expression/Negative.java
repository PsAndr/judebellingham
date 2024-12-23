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
    protected int getOperationResult(int val) {
        return -val;
    }

    @Override
    public float evaluateF(final Map<String, Float> variables) {
        return -expression.evaluateF(variables);
    }

    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    protected boolean canNearPlaceConst() {
        return false;
    }
}
