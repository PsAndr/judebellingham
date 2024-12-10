package expression;

public class Add extends BinaryExpression {
    public Add(final AllExpression left, final AllExpression right) {
        super(left, right);
    }

    @Override
    protected String getSign() {
        return "+";
    }

    @Override
    protected int getOperationResult(final int a, final int b) {
        return a + b;
    }

    @Override
    protected float getOperationResult(final float a, final float b) {
        return a + b;
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public boolean isChangeOperation() {
        return true;
    }
}
