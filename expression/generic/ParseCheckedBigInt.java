package expression.generic;

import java.math.BigInteger;

public class ParseCheckedBigInt implements ParserNumber<UncheckedBigInteger> {
    @Override
    public UncheckedBigInteger parse(String s) {
        return new UncheckedBigInteger(new BigInteger(s));
    }
}
