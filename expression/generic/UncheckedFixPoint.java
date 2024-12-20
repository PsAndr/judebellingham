package expression.generic;

public class UncheckedFixPoint extends BaseNumber<UncheckedFixPoint> {
    private final long value;
    private static final int SCALE = 16;


    private UncheckedFixPoint(final long value, final boolean isFixPoint) {
        if (isFixPoint) {
            this.value = value;
        } else {
            this.value = value << SCALE;
        }
    }

    public UncheckedFixPoint(final long value) {
        this(value, false);
    }

    public UncheckedFixPoint(final int value) {
        this(Long.valueOf(value));
    }

    private UncheckedFixPoint(final UncheckedFixPoint value) {
        this.value = value.value;
    }

    @Override
    public UncheckedFixPoint add(final UncheckedFixPoint b) {
        return new UncheckedFixPoint(value + b.value, true);
    }

    @Override
    public UncheckedFixPoint sub(final UncheckedFixPoint b) {
        return new UncheckedFixPoint(value - b.value, true);
    }

    @Override
    public UncheckedFixPoint negate() {
        return new UncheckedFixPoint(-value, true);
    }

    @Override
    public UncheckedFixPoint multiply(final UncheckedFixPoint b) {
        return new UncheckedFixPoint((value * b.value) >> SCALE, true);
    }

    @Override
    public UncheckedFixPoint divide(final UncheckedFixPoint b) {
        return new UncheckedFixPoint((value / b.value) << SCALE, true);
    }

    @Override
    public UncheckedFixPoint mod(UncheckedFixPoint b) {
        return new UncheckedFixPoint(value % b.value, true);
    }

    @Override
    public UncheckedFixPoint next() {
        return new UncheckedFixPoint(1).add(this);
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof UncheckedFixPoint o && value == o.value;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public UncheckedFixPoint copy() {
        return new UncheckedFixPoint(this);
    }

    @Override
    public int compareTo(UncheckedFixPoint o) {
        return Long.compare(value, o.value);
    }
}
