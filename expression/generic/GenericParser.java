package expression.generic;

public interface GenericParser<T extends BaseNumber<T>> {
    AllExpression<T> parse(final String expression);
}
