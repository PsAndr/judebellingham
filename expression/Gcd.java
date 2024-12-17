package expression;

public class Gcd extends BinaryExpression {
    public Gcd(final AllExpression left, final AllExpression right) {
        super(left, right);
    }

    @Override
    protected String getSign() {
        return "gcd";
    }

    private int mod(int a, int b) {
        int md = a % b;
        if (md < 0) {
            md = (md + b) % b;
        }
        return md;
    }

    @Override
    protected int getOperationResult(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (a != 0 && b != 0) {
            a = mod(a, b);
            if (a != 0) {
                b = mod(b, a);
            }
        }
        return a + b;
    }

    @Override
    protected float getOperationResult(final float a, final float b) {
        throw new ArithmeticException();
    }

    @Override
    public int getPriority() {
        return -1;
    }

    @Override
    public boolean isPriorityOperation() {
        return true;
    }

    @Override
    protected boolean isAssociative() {
        return true;
    }
}
