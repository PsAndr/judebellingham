package expression.exceptions;

import expression.Negative;
import expression.MaxExpression;

public class CheckedNegate extends Negative {
    public CheckedNegate(final MaxExpression expression) {
        super(expression);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int exprAns = expression.evaluate(x, y, z);
        if (exprAns == Integer.MIN_VALUE) {
            throw new ArithmeticException("overflow");
        }
        return -exprAns;
    }
}
