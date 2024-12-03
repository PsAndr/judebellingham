package expression;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface ToMiniString {
    default String toMiniString() {
        return toString();
    }

    default boolean equals(ToMiniString other) {
        if (other == null) {
            return false;
        }
        return toString().equals(other.toString());
    }

    default int getPriority() { return 0; }
    default boolean isChangeOperation() { return false; }
    default boolean isPriorityOperation() { return false; }
}
