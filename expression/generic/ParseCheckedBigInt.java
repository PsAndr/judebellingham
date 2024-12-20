package expression.generic;

import java.math.BigInteger;

public class ParseCheckedBigInt implements ParserNumber<CheckedBigInteger> {
    @Override
    public CheckedBigInteger parse(String s) {
        return new CheckedBigInteger(new BigInteger(s));
    }
}
