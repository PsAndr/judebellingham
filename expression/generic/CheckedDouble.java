package expression.generic;

public class CheckedDouble extends BaseNumber<CheckedDouble> {
    private final double value;

    public CheckedDouble(final double value) {
        this.value = value;
    }

    private CheckedDouble(final CheckedDouble value) {
        this(value.value);
    }

    @Override
    public CheckedDouble add(final CheckedDouble b) {
        return new CheckedDouble(value + b.value);
    }

    @Override
    public CheckedDouble sub(CheckedDouble b) {
        return new CheckedDouble(value - b.value);
    }

    @Override
    public CheckedDouble negate() {
        return new CheckedDouble(-value);
    }

    @Override
    public CheckedDouble multiply(final CheckedDouble b) {
        return new CheckedDouble(value * b.value);
    }

    @Override
    public CheckedDouble divide(final CheckedDouble b) {
        return new CheckedDouble(value / b.value);
    }

    @Override
    public CheckedDouble next() {
        return new CheckedDouble(1).add(this);
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof CheckedDouble o && value == o.value;
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
    public CheckedDouble copy() {
        return new CheckedDouble(value);
    }
}
