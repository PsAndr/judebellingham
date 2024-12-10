package expression;

public class Log extends BinaryExpression {
    public Log(final AllExpression left, final AllExpression right) {
        super(left, right);
    }

    @Override
    protected String getSign() {
        return "//";
    }

    @Override
    protected int getOperationResult(final int a, final int b) {
        if (a < 0 || b < 0) {
            return 0;
        }
        if (a == 0 && b != 0) {
            return Integer.MIN_VALUE;
        }
        if (a == 0) {
            return 0;
        }
        if (b == 0) {
            return 0;
        }
        if (b == 1 && a > 1) {
            return Integer.MAX_VALUE;
        }
        // System.err.println("LOG: " + a + " | " + b);
        int ans = 0;
        long mul = 1;
        while (mul < a) {
            ans++;
            mul *= b;
        }
        return mul > a ? ans - 1 : ans;
    }

    @Override
    protected float getOperationResult(final float a, final float b) {
        throw new ArithmeticException();
    }

    @Override
    public int getPriority() {
        return 3;
    }

    @Override
    public boolean isPriorityOperation() {
        return true;
    }
}
