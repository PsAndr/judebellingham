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
    default boolean isFlag() { return false; }  // todo: name normal need
    default boolean isFlag2() { return false; }  // todo: name normal need
}
