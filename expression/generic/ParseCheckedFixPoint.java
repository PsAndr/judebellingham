package expression.generic;

public class ParseCheckedFixPoint implements ParserNumber<UncheckedFixPoint> {
    @Override
    public UncheckedFixPoint parse(String s) {
        return new UncheckedFixPoint(Integer.parseInt(s));
    }
}
