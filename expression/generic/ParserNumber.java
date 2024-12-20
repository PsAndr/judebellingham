package expression.generic;

public interface ParserNumber<T extends BaseNumber<T>> {
    T parse(String s);
}
