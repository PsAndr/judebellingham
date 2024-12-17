package expression;

import java.util.Map;

public class Factorial extends UnaryExpression {
    public Factorial(final AllExpression expression) {
        super(expression);
    }

    @Override
    protected boolean isRight() {
        return true;
    }

    @Override
    protected String getSign() {
        return "!";
    }

    @Override
    protected int getOperationResult(int val) {
        return fact(val);
    }

    private int fact(int n) {
        if (n == Integer.MIN_VALUE) {
            return 0;
        }
        n = Math.abs(n);
        int ans = 1;
        for (int i = 2; i <= n; i++) {
            ans *= i;
            if (ans == 0) {
                return 0;
            }
        }
        return ans;
    }

    @Override
    public float evaluateF(final Map<String, Float> variables) {
        throw new ArithmeticException();
    }

    @Override
    public int getPriority() {
        return 9;
    }
}
