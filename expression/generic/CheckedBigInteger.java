package expression.generic;

import java.math.BigInteger;

public class CheckedBigInteger extends BaseNumber<CheckedBigInteger> {
    private final BigInteger value;

    public CheckedBigInteger(final int value) {
        this.value = BigInteger.valueOf(value);
    }

    public CheckedBigInteger(final BigInteger value) {
        this.value = value;
    }

    private CheckedBigInteger(final CheckedBigInteger value) {
        this(value.value);
    }

    @Override
    public CheckedBigInteger add(final CheckedBigInteger b) {
        return new CheckedBigInteger(value.add(b.value));
    }

    @Override
    public CheckedBigInteger sub(final CheckedBigInteger b) {
        return new CheckedBigInteger(value.subtract(b.value));
    }

    @Override
    public CheckedBigInteger negate() {
        return new CheckedBigInteger(value.negate());
    }

    @Override
    public CheckedBigInteger multiply(final CheckedBigInteger b) {
        return new CheckedBigInteger(value.multiply(b.value));
    }

    @Override
    public CheckedBigInteger divide(final CheckedBigInteger b) {
        return new CheckedBigInteger(value.divide(b.value));
    }

    @Override
    public CheckedBigInteger next() {
        return new CheckedBigInteger(1).add(this);
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof CheckedBigInteger o && value.equals(o.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public CheckedBigInteger copy() {
        return new CheckedBigInteger(value);
    }
}
