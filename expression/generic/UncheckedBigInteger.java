package expression.generic;

import java.math.BigInteger;

public class UncheckedBigInteger extends BaseNumber<UncheckedBigInteger> {
    private final BigInteger value;

    public UncheckedBigInteger(final int value) {
        this.value = BigInteger.valueOf(value);
    }

    public UncheckedBigInteger(final BigInteger value) {
        this.value = value;
    }

    private UncheckedBigInteger(final UncheckedBigInteger value) {
        this(value.value);
    }

    @Override
    public UncheckedBigInteger add(final UncheckedBigInteger b) {
        return new UncheckedBigInteger(value.add(b.value));
    }

    @Override
    public UncheckedBigInteger sub(final UncheckedBigInteger b) {
        return new UncheckedBigInteger(value.subtract(b.value));
    }

    @Override
    public UncheckedBigInteger negate() {
        return new UncheckedBigInteger(value.negate());
    }

    @Override
    public UncheckedBigInteger multiply(final UncheckedBigInteger b) {
        return new UncheckedBigInteger(value.multiply(b.value));
    }

    @Override
    public UncheckedBigInteger divide(final UncheckedBigInteger b) {
        return new UncheckedBigInteger(value.divide(b.value));
    }

    @Override
    public UncheckedBigInteger mod(UncheckedBigInteger b) {
        return new UncheckedBigInteger(value.mod(b.value));
    }

    @Override
    public UncheckedBigInteger next() {
        return new UncheckedBigInteger(1).add(this);
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof UncheckedBigInteger o && value.equals(o.value);
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
    public UncheckedBigInteger copy() {
        return new UncheckedBigInteger(this);
    }

    @Override
    public int compareTo(UncheckedBigInteger o) {
        return value.compareTo(o.value);
    }
}
