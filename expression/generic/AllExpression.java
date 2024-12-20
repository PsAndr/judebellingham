package expression.generic;

import java.util.Map;

public interface AllExpression<T extends BaseNumber<T>> {
    default int getPriority() { return 0; }
    default boolean isChangeOperation() { return false; }
    default boolean isPriorityOperation() { return false; }

    default String toMiniString() {
        return toString();
    }

    T evaluateNumber(final Map<String, T> mp);
}
