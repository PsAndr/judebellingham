package expression.exceptions;

import expression.Add;
import expression.AllExpression;

public class CheckedAdd extends Add {
    public CheckedAdd(final AllExpression left, final AllExpression right) {
        super(left, right);
    }

    @Override
    protected int getOperationResult(int a, int b) {
        if (b > 0 && Integer.MAX_VALUE - b < a) {
            throw new ArithmeticException("overflow");
        }
        if (b < 0 && Integer.MIN_VALUE - b > a) {
            throw new ArithmeticException("overflow");
        }
        return super.getOperationResult(a, b);
    }
}
