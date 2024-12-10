package expression;

public interface AllExpression extends Expression, TripleExpression, FloatMapExpression {
    default int getPriority() { return 0; }
    default boolean isChangeOperation() { return false; }
    default boolean isPriorityOperation() { return false; }
}
