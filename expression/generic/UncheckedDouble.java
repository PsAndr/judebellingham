package expression.generic;

public class UncheckedDouble extends BaseNumber<UncheckedDouble> {
    private final double value;

    public UncheckedDouble(final double value) {
        this.value = value;
    }

    private UncheckedDouble(final UncheckedDouble value) {
        this(value.value);
    }

    @Override
    public UncheckedDouble add(final UncheckedDouble b) {
        return new UncheckedDouble(value + b.value);
    }

    @Override
    public UncheckedDouble sub(final UncheckedDouble b) {
        return new UncheckedDouble(value - b.value);
    }

    @Override
    public UncheckedDouble negate() {
        return new UncheckedDouble(-value);
    }

    @Override
    public UncheckedDouble multiply(final UncheckedDouble b) {
        return new UncheckedDouble(value * b.value);
    }

    @Override
    public UncheckedDouble divide(final UncheckedDouble b) {
        return new UncheckedDouble(value / b.value);
    }

    @Override
    public UncheckedDouble mod(final UncheckedDouble b) {
        return new UncheckedDouble(value % b.value);
    }

    @Override
    public UncheckedDouble next() {
        return new UncheckedDouble(1).add(this);
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof UncheckedDouble o && value == o.value;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public UncheckedDouble copy() {
        return new UncheckedDouble(this);
    }

    @Override
    public int compareTo(final UncheckedDouble o) {
        return Double.compare(value, o.value);
    }
}
