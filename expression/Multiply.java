package expression;

public class Multiply extends BinaryExpression {
    public Multiply(final MaxExpression left, final MaxExpression right) {
        super(left, right);
    }

    @Override
    protected String getSign() {
        return "*";
    }

    @Override
    protected int getOperationResult(final int a, final int b) {
        return a * b;
    }

    @Override
    protected float getOperationResult(final float a, final float b) {
        return a * b;
    }

    @Override
    public int getPriority() {
        return 2;
    }

    @Override
    public boolean isChangeOperation() {
        return true;
    }
}