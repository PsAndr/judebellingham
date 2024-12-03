package expression.parser;

import expression.*;

import java.util.EnumMap;
import java.util.Map;

public class ExpressionParser implements TripleParser {
    private enum Operations {
        Add(1, "+", false),
        Subtract(1, "-", false),
        Multiply(2, "*", false),
        Divide(2, "/", false),
        Pow(4, "**", false),
        Log(4, "//", false),
        Factorial(5, "!", true),
        Minus(5, "-", true);

        public final int priority;
        public final String stringVal;
        public final boolean isUnary;

        Operations(int priority, String stringVal, boolean isUnary) {
            this.priority = priority;
            this.stringVal = stringVal;
            this.isUnary = isUnary;
        }
    }

    @Override
    public TripleExpression parse(String expression) {
        return new Const(0);
    }
}
