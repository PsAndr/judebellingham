package expression;

import java.util.Map;

public class Factorial implements SimpleRightExpression {
    public MaxExpression expression;

    public Factorial(MaxExpression expression) {
        this.expression = expression;
    }

    @Override
    public int evaluate(int x) {
        return fact(expression.evaluate(x));
    }

    private int fact(int n) {
        if (n == Integer.MIN_VALUE) {
            return 0;
        }
        if (n == Integer.MAX_VALUE) {
            return 0;
        }
        n = Math.abs(n);
        int ans = 1;
        // System.err.println("FACT: " + n);
        for (int i = 2; i <= n; i++) {
            ans *= i;
            if (ans == 0) {
                return 0;
            }
        }
        return ans;
    }

    @Override
    public float evaluateF(Map<String, Float> variables) {
        throw new ArithmeticException();
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return fact(expression.evaluate(x, y, z));
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String toMiniString() {
        String ans;
        if (expression instanceof SimpleRightExpression || expression instanceof SimpleExpression) {
            ans = expression.toMiniString() + "!";
        } else {
            ans = "(" + expression.toMiniString() + ")!";
        }
        // System.err.println("TO MINI STRING::: " + this + " | " + ans);
        return ans;
    }

    @Override
    public String toString() {
        return "(" + expression + ")!";
    }
}
