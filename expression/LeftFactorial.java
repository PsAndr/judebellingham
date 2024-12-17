package expression;

import java.util.Map;

public class LeftFactorial extends UnaryExpression {
    public LeftFactorial(final AllExpression expression) {
        super(expression);
    }

    @Override
    protected boolean isRight() {
        return false;
    }

    @Override
    protected String getSign() {
        return "!";
    }

    @Override
    protected int getOperationResult(int val) {
        int ans = 1;
        for (int i = 1;; i++) {
            if (ans > val / i) {
                return i - 1;
            }
            ans *= i;
        }
    }

    @Override
    public float evaluateF(final Map<String, Float> variables) {
        return -expression.evaluateF(variables);
    }

    @Override
    public int getPriority() {
        return 10;
    }
}
