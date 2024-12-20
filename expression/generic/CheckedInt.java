package expression.generic;

public class CheckedInt extends BaseNumber<CheckedInt> {
    private final int value;

    public CheckedInt(final int value) {
        this.value = value;
    }

    private CheckedInt(final CheckedInt value) {
        this(value.value);
    }

    @Override
    public CheckedInt add(final CheckedInt b) {
        if (b.value > 0 && Integer.MAX_VALUE - b.value < value) {
            throw new ArithmeticException("overflow");
        }
        if (b.value < 0 && Integer.MIN_VALUE - b.value > value) {
            throw new ArithmeticException("overflow");
        }
        return new CheckedInt(value + b.value);
    }

    @Override
    public CheckedInt negate() {
        if (value == Integer.MIN_VALUE) {
            throw new ArithmeticException("overflow");
        }
        return new CheckedInt(-value);
    }

    @Override
    public CheckedInt multiply(final CheckedInt b) {
        int res = value * b.value;
        if (b.value != 0 && value != 0 && (res / b.value != value || res / value != b.value)) {
            throw new ArithmeticException("overflow");
        }
        return new CheckedInt(res);
    }

    @Override
    public CheckedInt divide(final CheckedInt b) {
        if (b.value == 0) {
            throw new ArithmeticException("division by zero");
        }
        if (value == Integer.MIN_VALUE && b.value == -1) {
            throw new ArithmeticException("overflow");
        }
        return new CheckedInt(value / b.value);
    }

    @Override
    public CheckedInt mod(CheckedInt b) {
        int res = value % b.value;
        return new CheckedInt(res);
    }

    @Override
    public CheckedInt next() {
        return new CheckedInt(1).add(this);
    }

    @Override
    public CheckedInt sub(CheckedInt b) {
        if (b.value > 0 && Integer.MIN_VALUE + b.value > value) {
            throw new ArithmeticException("overflow");
        }
        if (b.value < 0 && Integer.MAX_VALUE + b.value < value) {
            throw new ArithmeticException("overflow");
        }
        return new CheckedInt(value - b.value);
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof CheckedInt o && value == o.value;
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
    public CheckedInt copy() {
        return new CheckedInt(value);
    }

    @Override
    public int compareTo(CheckedInt o) {
        return Integer.compare(value, o.value);
    }
}
