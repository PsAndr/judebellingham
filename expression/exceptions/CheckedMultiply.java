package expression.exceptions;

import expression.AllExpression;
import expression.Multiply;

public class CheckedMultiply extends Multiply {
    public CheckedMultiply(final AllExpression left, final AllExpression right) {
        super(left, right);
    }

    @Override
    protected int getOperationResult(int a, int b) {
        int res = super.getOperationResult(a, b);
        if (b != 0 && a != 0 && (res / b != a || res / a != b)) {
            throw new ArithmeticException("overflow");
        }
        return res;
    }
}
