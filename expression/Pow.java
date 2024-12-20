package expression;

public class Pow extends BinaryExpression {
    public Pow(final AllExpression left, final AllExpression right) {
        super(left, right);
    }

    @Override
    protected String getSign() {
        return "**";
    }

    @Override
    protected int getOperationResult(final int a, final int b) {
        int ans = 1;
        for (int i = 1; i <= b; i++) {
            ans *= a;
            if (ans == 0) {
                break;
            }
        }
        return ans;
    }

    @Override
    protected float getOperationResult(final float a, final float b) {
        return Double.valueOf(Math.pow(a, b)).floatValue();
    }

    @Override
    public int getPriority() {
        return 3;
    }

    @Override
    public boolean isPriorityOperation() {
        return true;
    }

    @Override
    protected boolean isAssociative() {
        return false;
    }
}
