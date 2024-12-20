package expression.generic;

import base.Named;
import base.Selector;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.function.*;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class GenericTest {
    private static final Consumer<GenericTester> ADD = binary("+", 200);
    private static final Consumer<GenericTester> SUBTRACT = binary("-", -200);
    private static final Consumer<GenericTester> MULTIPLY = binary("*", 301);
    private static final Consumer<GenericTester> DIVIDE = binary("/", -300);
    private static final Consumer<GenericTester> NEGATE = unary("-");

    // === Checked integers
    private static Integer i(final long v) {
        if (v != (int) v) {
            throw new ArithmeticException("Overflow");
        }
        return (int) v;
    }

    private static final Mode<Integer> INTEGER_CHECKED = mode("i", c -> c)
            .binary("+", (a, b) -> i(a + (long) b))
            .binary("-", (a, b) -> i(a - (long) b))
            .binary("*", (a, b) -> i(a * (long) b))
            .binary("/", (a, b) -> i(a / (long) b))
            .unary("-", a -> i(- (long) a))

            .unary("abs", a -> i(Math.abs((long) a)))
            .unary("square", a -> i(a * (long) a))
            .binary("mod", (a, b) -> i(a % (long) b))
            ;

    // === Doubles

    @SuppressWarnings("Convert2MethodRef")
    private static final Mode<Double> DOUBLE = mode("d", c -> (double) c)
            .binary("+", (a, b) -> a + b)
            .binary("-", (a, b) -> a - b)
            .binary("*", (a, b) -> a * b)
            .binary("/", (a, b) -> a / b)
            .unary("-", a -> -a)

            .unary("abs", Math::abs)
            .unary("square", a -> a * a)
            .binary("mod", (a, b) -> a % b)
            ;

    // === BigIntegers

    private static final Mode<BigInteger> BIG_INTEGER = mode("bi", BigInteger::valueOf)
            .binary("+", BigInteger::add)
            .binary("-", BigInteger::subtract)
            .binary("*", BigInteger::multiply)
            .binary("/", BigInteger::divide)
            .unary("-", BigInteger::negate)

            .unary("abs", BigInteger::abs)
            .unary("square", a -> a.multiply(a))
            .binary("mod", BigInteger::mod)
            ;

    // === Parens
    private static final Consumer<GenericTester> PARENS = tester -> tester.parens("{", "}", "[", "]");

    // === Asm
    private static final Consumer<GenericTester> ABS = unary("abs");
    private static final Consumer<GenericTester> SQUARE = unary("square");
    private static final Consumer<GenericTester> MOD = binary("mod", -300);

    // === Unchecked integers

    @SuppressWarnings("Convert2MethodRef")
    private static final Mode<Integer> INTEGER_UNCHECKED = mode("u", c -> c)
            .binary("+", (a, b) -> a + b)
            .binary("-", (a, b) -> a - b)
            .binary("*", (a, b) -> a * b)
            .binary("/", (a, b) -> a / b)
            .unary("-", a -> -a)

            .unary("abs", Math::abs)
            .unary("square", a -> a * a)
            .binary("mod", (a, b) -> a % b)
            ;


    // === Longs

    @SuppressWarnings("Convert2MethodRef")
    private static final Mode<Long> LONG = mode("l", c -> (long) c)
            .binary("+", (a, b) -> a + b)
            .binary("-", (a, b) -> a - b)
            .binary("*", (a, b) -> a * b)
            .binary("/", (a, b) -> a / b)
            .unary("-", a -> -a)

            .unary("abs", Math::abs)
            .unary("square", a -> a * a)
            .binary("mod", (a, b) -> a % b)
            ;

    // === Fixed-point integers

    private static final int FIXED = 16;
    @SuppressWarnings("Convert2MethodRef")
    private static final Mode<Integer> INTEGER_FIXED = mode("if", a -> a << FIXED)
            .binary("+", (a, b) -> a + b)
            .binary("-", (a, b) -> a - b)
            .binary("*", (a, b) -> (a * b) >> FIXED)
            .binary("/", (a, b) -> (a / b) << FIXED)
            .unary("-", a -> -a)

            .unary("abs", Math::abs)
            .unary("square", a -> (a * a) >> FIXED)
            .binary("mod", (a, b) -> a % b)
            ;

    // === Fixed-point longs

    @SuppressWarnings("Convert2MethodRef")
    private static final Mode<Long> LONG_FIXED = mode("lf", a -> (long) a << FIXED)
            .binary("+", (a, b) -> a + b)
            .binary("-", (a, b) -> a - b)
            .binary("*", (a, b) -> (a * b) >> FIXED)
            .binary("/", (a, b) -> (a / b) << FIXED)
            .unary("-", a -> -a)

            .unary("abs", Math::abs)
            .unary("square", a -> (a * a) >> FIXED)
            .binary("mod", (a, b) -> a % b)
            ;


    // === Common
    private GenericTest() {
    }

    /* package-private */ static Consumer<GenericTester> unary(final String name) {
        return tester -> tester.unary(name, 1);
    }

    /* package-private */ static Consumer<GenericTester> binary(final String name, final int priority) {
        return tester -> tester.binary(name, priority);
    }

    private static <T> Mode<T> mode(final String mode, final IntFunction<T> constant) {
        return new Mode<>(mode, constant, IntUnaryOperator.identity());
    }

    public static final Selector SELECTOR = Selector.composite(GenericTest.class, GenericTester::new, "easy", "hard")
            .variant("Base", INTEGER_CHECKED, DOUBLE, BIG_INTEGER, ADD, SUBTRACT, MULTIPLY, DIVIDE, NEGATE)
            .variant("Parens", PARENS)
            .variant("AsmUl", PARENS, ABS, SQUARE, MOD, INTEGER_UNCHECKED, LONG)
            .variant("AsmIf", PARENS, ABS, SQUARE, MOD, INTEGER_UNCHECKED, INTEGER_FIXED)
            .variant("AsmLf", PARENS, ABS, SQUARE, MOD, INTEGER_UNCHECKED, LONG_FIXED)
            .variant("Ul", PARENS, INTEGER_UNCHECKED, LONG)
            .selector();

    public static void main(final String... args) {
        SELECTOR.main(args);
    }

    /* package-private */ static class Mode<T> implements Consumer<GenericTester> {
        private final String mode;
        private final IntFunction<T> constant;
        private final List<Named<UnaryOperator<GenericTester.F<T>>>> unary = new ArrayList<>();
        private final List<Named<BinaryOperator<GenericTester.F<T>>>> binary = new ArrayList<>();
        private final IntUnaryOperator fixer;

        public Mode(final String mode, final IntFunction<T> constant, final IntUnaryOperator fixer) {
            this.mode = mode;
            this.constant = constant;
            this.fixer = fixer;
        }

        public Mode<T> unary(final String name, final UnaryOperator<T> op) {
            unary.add(Named.of(name, arg -> (x, y, z) -> op.apply(arg.apply(x, y, z))));
            return this;
        }

        public Mode<T> binary(final String name, final BinaryOperator<T> op) {
            binary.add(Named.of(name, (a, b) -> (x, y, z) -> op.apply(a.apply(x, y, z), b.apply(x, y, z))));
            return this;
        }

        @Override
        public void accept(final GenericTester tester) {
            tester.mode(mode, constant, unary, binary, fixer);
        }
    }
}
