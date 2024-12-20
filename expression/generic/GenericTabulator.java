package expression.generic;

import expression.parser.WrongExpressionException;
import java.util.Map;

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
                    new UncheckedDouble(x1), x2 - x1 + 1,
                    new UncheckedDouble(y1), y2 - y1 + 1,
                    new UncheckedDouble(z1), z2 - z1 + 1);
            case "bi" -> tabulate(new ExpressionParser<>(new ParseCheckedBigInt()), expression,
                    new UncheckedBigInteger(x1), x2 - x1 + 1,
                    new UncheckedBigInteger(y1), y2 - y1 + 1,
                    new UncheckedBigInteger(z1), z2 - z1 + 1);
            case "u" -> tabulate(new ExpressionParser<>(new ParseUncheckedInt()), expression,
                    new UncheckedInt(x1), x2 - x1 + 1,
                    new UncheckedInt(y1), y2 - y1 + 1,
                    new UncheckedInt(z1), z2 - z1 + 1);
            case "lf" -> tabulate(new ExpressionParser<>(new ParseCheckedFixPoint()), expression,
                    new UncheckedFixPoint(x1), x2 - x1 + 1,
                    new UncheckedFixPoint(y1), y2 - y1 + 1,
                    new UncheckedFixPoint(z1), z2 - z1 + 1);
            default -> throw new TabulatorException("Unknown mode: " + mode);
        };
    }

    private <T extends BaseNumber<T>> Object[][][] tabulate(final ExpressionParser<T> parser, final String expression,
                                                           final T x1, final int x2, final T y1, final int y2,
                                                           final T z1, final int z2) throws Exception {
        final Object[][][] answer = new Object[x2][y2][z2];
        final AllExpression<T> expr;
        try {
            expr = parser.parse(expression);
        } catch (WrongExpressionException e) {
            throw new TabulatorException("Error parsing expression (" + expression + "): " + e.getMessage());
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

    public static void main(final String[] args) {
        if (args.length != 2 || !args[0].startsWith("-")) {
            System.err.println("Usage: GenericTabulator -<mode> <expression>");
            return;
        }
        final String mode = args[0].substring(1);
        final String expression = args[1];
        final GenericTabulator tabulator = new GenericTabulator();
        try {
            final Object[][][] res = tabulator.tabulate(mode, expression, -2, 2, -2, 2, -2, 2);
            for (int i = 0; i < res.length; i++) {
                for (int j = 0; j < res[i].length; j++) {
                    for (int k = 0; k < res[i].length; k++) {
                        System.out.printf("%d, %d, %d (x, y, z) = %s%n", i - 2, j - 2, k - 2, res[i][j][k]);
                    }
                    System.out.println();
                }
                System.out.println();
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
