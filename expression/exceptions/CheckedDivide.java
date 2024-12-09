package expression.exceptions;

import expression.Divide;
import expression.MaxExpression;

public class CheckedDivide extends Divide {
    public CheckedDivide(final MaxExpression left, final MaxExpression right) {
        super(left, right);
    }

    @Override
    protected int getOperationResult(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("division by zero");
        }
        if (a == Integer.MIN_VALUE && b == -1) {
            throw new ArithmeticException("overflow");
        }
        return super.getOperationResult(a, b);
    }
}
