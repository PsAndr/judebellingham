package expression.generic;

public class ParseCheckedInt implements ParserNumber<CheckedInt> {
    @Override
    public CheckedInt parse(final String s) {
        return new CheckedInt(Integer.parseInt(s));
    }
}
