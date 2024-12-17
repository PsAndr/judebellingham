package expression;

public class Lcm extends Gcd {
    public Lcm(final AllExpression left, final AllExpression right) {
        super(left, right);
    }

    @Override
    protected String getSign() {
        return "lcm";
    }

    @Override
    protected int getOperationResult(final int a, final int b) {
        int gcd = super.getOperationResult(a, b);
        if (gcd == 0 || a == 0 || b == 0) {
            return 0;
        }
        int tmp = a / gcd;
        int res = tmp * b;
        if (tmp != 0 && (res / b != tmp || res / tmp != b)) {
            throw new ArithmeticException("overflow");
        }
        return res;
    }
}
