package expression.generic;

public class ParseCheckedDouble implements ParserNumber<UncheckedDouble> {
    @Override
    public UncheckedDouble parse(final String s) {
        return new UncheckedDouble(Double.parseDouble(s));
    }
}
