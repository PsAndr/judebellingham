package expression.generic;

public class UncheckedInt extends BaseNumber<UncheckedInt> {
    private final int value;

    public UncheckedInt(final int value) {
        this.value = value;
    }

    private UncheckedInt(final UncheckedInt value) {
        this(value.value);
    }

    @Override
    public UncheckedInt add(final UncheckedInt b) {
        return new UncheckedInt(value + b.value);
    }

    @Override
    public UncheckedInt negate() {
        return new UncheckedInt(-value);
    }

    @Override
    public UncheckedInt multiply(final UncheckedInt b) {
        return new UncheckedInt(value * b.value);
    }

    @Override
    public UncheckedInt divide(final UncheckedInt b) {
        return new UncheckedInt(value / b.value);
    }

    @Override
    public UncheckedInt mod(final UncheckedInt b) {
        return new UncheckedInt(value % b.value);
    }

    @Override
    public UncheckedInt next() {
        return new UncheckedInt(1).add(this);
    }

    @Override
    public UncheckedInt sub(final UncheckedInt b) {
        return new UncheckedInt(value - b.value);
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof UncheckedInt o && value == o.value;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public UncheckedInt copy() {
        return new UncheckedInt(this);
    }

    @Override
    public int compareTo(final UncheckedInt o) {
        return Integer.compare(value, o.value);
    }
}
