package expression.parser;

import base.ExtendedRandom;
import base.Pair;
import base.Selector;
import expression.TripleExpression;
import expression.Variable;

import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static expression.parser.Operations.*;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class ParserTest {
    private static final TripleParser PARSER = new ExpressionParser();
    private static final Consumer<ParserTester> TRIPLE = kind(
            TripleExpression.KIND,
            (expr, variables) -> PARSER.parse(expr)
    );

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

    // === Common

    public static final Selector SELECTOR = Selector.composite(ParserTest.class, ParserTester::new, "easy", "hard")
            .variant("Base", TRIPLE, ADD, SUBTRACT, MULTIPLY, DIVIDE, NEGATE)
            .variant("LastVars", triple("rn"))
            .variant("PowLog", POW_O, LOG_O)
            .variant("Square", SQUARE)
            .variant("Cube", CUBE)
            .variant("Factorial", FACTORIAL_O)
            .variant("Sqrt", SQRT_O)
            .variant("Cbrt", CBRT)
            .selector();

    private ParserTest() {
    }

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
