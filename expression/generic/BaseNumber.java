package expression.generic;

public abstract class BaseNumber<T extends BaseNumber<T>> implements Comparable<T> {
    public abstract T add(final T b);
    public abstract T sub(final T b);
    public abstract T multiply(final T b);
    public abstract T divide(final T b);
    public abstract T mod(final T b);

    public abstract T negate();

    public abstract T next();

    public abstract Object getValue();

    public abstract T copy();
}
