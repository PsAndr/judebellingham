package expression.generic;

public class ParseCheckedDouble implements ParserNumber<CheckedDouble> {
    @Override
    public CheckedDouble parse(String s) {
        return new CheckedDouble(Double.parseDouble(s));
    }
}
