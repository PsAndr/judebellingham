package expression;

public class Pow extends BinaryExpression {
    public Pow(final MaxExpression left, final MaxExpression right) {
        super(left, right);
    }

    @Override
    protected String getSign() {
        return "**";
    }

    @Override
    protected int getOperationResult(final int a, final int b) {
        if (b == 0) {
            return 1;
        }
        int ans = getOperationResult(a, b / 2);
        ans *= ans;
        if (b % 2 == 1) {
            ans *= a;
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
}
