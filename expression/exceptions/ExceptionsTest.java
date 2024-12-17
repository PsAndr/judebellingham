package expression.exceptions;

import base.ExtendedRandom;
import base.Pair;
import base.Selector;
import expression.TripleExpression;
import expression.Variable;
import expression.parser.Operations;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static expression.parser.Operations.*;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class ExceptionsTest {
    private static final ExpressionParser PARSER = new ExpressionParser();
    private static final Operation TRIPLE = kind(TripleExpression.KIND, (expr, variables) -> PARSER.parse(expr));

    private static final String LETTERS = ExtendedRandom.ENGLISH.substring(0, 23) + ExtendedRandom.ENGLISH.toUpperCase().substring(1, 23);
    private static Operations.Operation triple(final String pattern) {
        return kind(
                TripleExpression.KIND.withVariables((random, count) -> Stream.of("x", "y", "z")
                        .map(name -> pattern.replace("n", name).replace("r", random.randomString(LETTERS)))
                        .map(name -> Pair.<String, TripleExpression>of(name, new Variable(name)))
                        .collect(Collectors.toUnmodifiableList())
                ),
                (expr, variables) -> PARSER.parse(expr)
        );
    }

    public static final Selector SELECTOR = Selector.composite(ExceptionsTest.class, ExceptionsTester::new, "easy", "hard")
            .variant("Base", TRIPLE, ADD, SUBTRACT, MULTIPLY, DIVIDE, NEGATE)
            .variant("LastVars", triple("rn"))
            .variant("GcdLcm", GCD, LCM)
            .variant("Square", SQUARE, TETRA_2)
            .variant("Cube", CUBE, TETRA_3)
            .variant("Factorials", FACTORIAL_C, PRE_FACTORIAL)
            .variant("Geom", AREA, PERIMETER)
            .variant("Sqrt", SQRT_C)
            .selector();

    private ExceptionsTest() {
    }

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
