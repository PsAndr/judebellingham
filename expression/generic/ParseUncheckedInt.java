package expression.generic;

public class ParseUncheckedInt implements ParserNumber<UncheckedInt> {
    @Override
    public UncheckedInt parse(String s) {
        return new UncheckedInt(Integer.parseInt(s));
    }
}
