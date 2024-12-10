package expression.exceptions;

import expression.AllExpression;
import expression.Subtract;

public class CheckedSubtract extends Subtract {
    public CheckedSubtract(final AllExpression left, final AllExpression right) {
        super(left, right);
    }

    @Override
    protected int getOperationResult(int a, int b) {
        if (b > 0 && Integer.MIN_VALUE + b > a) {
            throw new ArithmeticException("overflow");
        }
        if (b < 0 && Integer.MAX_VALUE + b < a) {
            throw new ArithmeticException("overflow");
        }
        return super.getOperationResult(a, b);
    }
}
