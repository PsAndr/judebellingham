package expression;

public interface MaxExpression extends Expression, TripleExpression, FloatMapExpression {
    default int getPriority() { return 0; }
    default boolean isChangeOperation() { return false; }
    default boolean isPriorityOperation() { return false; }
}
