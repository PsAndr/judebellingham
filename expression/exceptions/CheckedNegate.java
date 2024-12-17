package expression.exceptions;

import expression.Negative;
import expression.AllExpression;

public class CheckedNegate extends Negative {
    public CheckedNegate(final AllExpression expression) {
        super(expression);
    }

    @Override
    protected int getOperationResult(int val) {
        if (val == Integer.MIN_VALUE) {
            throw new ArithmeticException("overflow");
        }
        return -val;
    }
}
