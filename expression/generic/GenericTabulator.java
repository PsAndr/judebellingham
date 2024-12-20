package expression.generic;

import expression.parser.WrongExpressionException;

import java.util.Map;
import java.util.Set;

public class GenericTabulator implements Tabulator {
    @Override
    public Object[][][] tabulate(final String mode, final String expression,
                                 final int x1, final int x2, final int y1, final int y2, final int z1, final int z2)
            throws Exception {
        return switch (mode) {
            case "i" -> tabulate(new ExpressionParser<>(new ParseCheckedInt()), expression,
                    new CheckedInt(x1), x2 - x1 + 1,
                    new CheckedInt(y1), y2 - y1 + 1,
                    new CheckedInt(z1), z2 - z1 + 1);
            case "d" -> tabulate(new ExpressionParser<>(new ParseCheckedDouble()), expression,
                    new CheckedDouble(x1), x2 - x1 + 1,
                    new CheckedDouble(y1), y2 - y1 + 1,
                    new CheckedDouble(z1), z2 - z1 + 1);
            case "bi" -> tabulate(new ExpressionParser<>(new ParseCheckedBigInt()), expression,
                    new CheckedBigInteger(x1), x2 - x1 + 1,
                    new CheckedBigInteger(y1), y2 - y1 + 1,
                    new CheckedBigInteger(z1), z2 - z1 + 1);
            default -> throw new Exception("Unknown mode");
        };
    }

    public <T extends BaseNumber<T>> Object[][][] tabulate(final ExpressionParser<T> parser, final String expression,
                                                           final T x1, final int x2, final T y1, final int y2,
                                                           final T z1, final int z2) throws Exception {
        final Object[][][] answer = new Object[x2][y2][z2];
        final AllExpression<T> expr;
        try {
            expr = parser.parse(expression);
        } catch (RuntimeException e) {
            System.out.println("Error parsing expression (" + expression + "): " + e.getMessage());
            throw new Exception();
        }

        T xVal = x1.copy();
        for (int x = 0; x < x2; x++) {
            T yVal = y1.copy();
            for (int y = 0; y < y2; y++) {
                T zVal = z1.copy();
                for (int z = 0; z < z2; z++) {
                    try {
                        answer[x][y][z] = expr.evaluateNumber(Map.of(
                                "x", xVal, "y", yVal, "z", zVal)).getValue();
                        // System.out.println(x + ", " + y + ", " + z + ": " + answer[x][y][z]);
                    } catch (RuntimeException ignored) {

                    }
                    zVal = zVal.next();
                }
                yVal = yVal.next();
            }
            xVal = xVal.next();
        }
        return answer;
    }
}
