package expression.parser;

import expression.*;

import java.util.List;
import java.util.Objects;

public class ExpressionParser implements TripleParser {
    public static class WrongExpressionException extends RuntimeException {
    }

    private enum Operation {
        Add(1, "+", false),
        Subtract(1, "-", false),
        Multiply(2, "*", false),
        Divide(2, "/", false),
        Pow(4, "**", false),
        Log(4, "//", false),
        Factorial(6, "!", true, true),
        Minus(7, "-", true),
        ;

        public final int priority;
        public final String stringVal;
        public final boolean isUnary;
        public final boolean isRight;

        Operation(int priority, String stringVal, boolean isUnary) {
            this(priority, stringVal, isUnary, false);
        }

        Operation(int priority, String stringVal, boolean isUnary, boolean isRight) {
            this.priority = priority;
            this.stringVal = stringVal;
            this.isUnary = isUnary;
            this.isRight = isRight;
        }
    }

    private enum Token {
        ADD(List.of(Operation.Add)),
        MULTIPLY(List.of(Operation.Multiply)),
        DIVIDE(List.of(Operation.Divide)),
        POW(List.of(Operation.Pow)),
        LOG(List.of(Operation.Log)),
        FACTORIAL(List.of(Operation.Factorial)),
        MINUS(List.of(Operation.Minus, Operation.Subtract)),

        CONST(List.of()),
        VARIABLE(List.of()),

        OPEN_BRACKET(List.of()),
        CLOSE_BRACKET(List.of()),;

        public final List<Operation> operations;
        Token(final List<Operation> operations) {
            this.operations = operations;
        }
    }

    private static class TokenVal {
        public final Token token;
        public final String value;
        public final int rightBound;

        public TokenVal(final Token token, final String value, final int rightBound) {
            this.token = token;
            this.value = value;
            this.rightBound = rightBound;
        }
    }

    @Override
    public TripleExpression parse(String expression) {
        // System.err.println(expression);
        TripleExpression ans = Objects.requireNonNull(parse(expression, 0, -1)).expression;
        // System.err.println("ANS :::: " + ans.toMiniString());
        return ans;
    }

    static class ParsePart {
        public final MaxExpression expression;
        public final int rBound;

        public ParsePart(MaxExpression expression, int rBound) {
            this.expression = expression;
            this.rBound = rBound;
        }

        @Override
        public String toString() {
            return expression.toMiniString() + "(" + rBound + ")";
        }
    }

    private TokenVal nextToken(String expression, int leftBound, boolean unaryAble) {
        for (int i = leftBound; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (Character.isWhitespace(c)) {
                continue;
            }
            if (Character.isDigit(c) ||
                    (c == '-' && i + 1 < expression.length() && Character.isDigit(expression.charAt(i + 1))
                    && unaryAble)) {
                int j = i + 1;
                StringBuilder sb = new StringBuilder().append(c);
                while (j < expression.length() && Character.isDigit(expression.charAt(j))) {
                    sb.append(expression.charAt(j++));
                }
                return new TokenVal(Token.CONST, sb.toString(), j - 1);
            }
            if (c == '(') {
                return new TokenVal(Token.OPEN_BRACKET, "(", i);
            }
            if (c == ')') {
                return new TokenVal(Token.CLOSE_BRACKET, ")", i);
            }
            if (Character.isLetter(c)) {
                int j = i;
                StringBuilder sb = new StringBuilder();
                while (j < expression.length() && Character.isLetter(expression.charAt(j))) {
                    sb.append(expression.charAt(j++));
                }
                j--;
                return new TokenVal(Token.VARIABLE, sb.toString(), j);
            }
            Token ans = null;
            String op = "";
            for (Token token : Token.values()) {
                if (token.operations.isEmpty()) {
                    continue;
                }
                for (Operation operation : token.operations) {
                    if (operation.isUnary && !operation.isRight && !unaryAble) {
                        continue;
                    }
                    if (operation.stringVal.startsWith(String.valueOf(c))) {
                        boolean flag = true;
                        for (int j = 0; j < operation.stringVal.length(); j++) {
                            if (i + j >= expression.length()) {
                                flag = false;
                                break;
                            }
                            if (expression.charAt(i + j) != operation.stringVal.charAt(j)) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            if (ans == null || op.length() < operation.stringVal.length()) {
                                ans = token;
                                op = operation.stringVal;
                            }
                        }
                    }
                }
            }
            return new TokenVal(ans, op, i + op.length() - 1);
        }
        return null;
    }

    private int IND = 0;

    private ParsePart parse(final String expression, int leftBound, final int minPriority) {
        int ind = IND++;
        // System.err.println(minPriority + " | " + leftBound +
        //         " = " + expression.substring(leftBound) + "(" + ind + ")");
        ParsePart prevPart = null;
        while (true) {
            // System.err.println("LOL");
            TokenVal tokenVal = nextToken(expression, leftBound, prevPart == null);
            if (tokenVal == null) {
                // System.err.println("RETURN ||| " + leftBound + " : END");
                if (prevPart == null) {
                    return null;
                }
                return new ParsePart(prevPart.expression, expression.length());
            }
            // System.err.println("Token: " + tokenVal.value + " | " + tokenVal.rightBound + " : "
            // + ind + " (" + prevPart + ") ");

            leftBound = tokenVal.rightBound + 1;
            Token token = tokenVal.token;
            switch (token) {
                case OPEN_BRACKET:
                    ParsePart inBracket = parse(expression, tokenVal.rightBound + 1, -1);
                    prevPart = new ParsePart(inBracket.expression, inBracket.rBound + 1);
                    leftBound = prevPart.rBound + 1;
                    continue;
                case CLOSE_BRACKET:
                    if (prevPart == null) {
                        throw new WrongExpressionException();
                    }
                    // System.err.println("RETURN ||| " + "BRACKET " + leftBound);
                    return new ParsePart(prevPart.expression, tokenVal.rightBound - 1);
                case VARIABLE:
                    prevPart = new ParsePart(new Variable(tokenVal.value), tokenVal.rightBound);
                    continue;
                case CONST:
                    prevPart = new ParsePart(new Const(
                            (int)(Long.parseLong(tokenVal.value))), tokenVal.rightBound);
                    continue;
            }

            final Operation operation = getOperation(token, prevPart);
            if (operation.priority < minPriority) {
                // System.err.println("RETURN ||| " + operation + " : " + tokenVal.value + " : " + tokenVal.rightBound
                //         + " : " + tokenVal.token + " : " + leftBound);
                return prevPart;
            }
            ParsePart nextPart = null;
            if (!operation.isUnary) {
                nextPart = parse(expression, tokenVal.rightBound + 1, operation.priority + 1);
                if (nextPart == null) {
                    throw new WrongExpressionException();
                }
                leftBound = nextPart.rBound + 1;
            }
            switch (operation) {
                case Add:
                    prevPart = new ParsePart(new Add(prevPart.expression, nextPart.expression),
                            nextPart.rBound);
                    break;
                case Subtract:
                    prevPart = new ParsePart(new Subtract(prevPart.expression, nextPart.expression),
                            nextPart.rBound);
                    break;
                case Multiply:
                    prevPart = new ParsePart(new Multiply(prevPart.expression, nextPart.expression),
                            nextPart.rBound);
                    break;
                case Divide:
                    prevPart = new ParsePart(new Divide(prevPart.expression, nextPart.expression),
                            nextPart.rBound);
                    break;
                case Pow:
                    prevPart = new ParsePart(new Pow(prevPart.expression, nextPart.expression),
                            nextPart.rBound);
                    break;
                case Log:
                    prevPart = new ParsePart(new Log(prevPart.expression, nextPart.expression),
                            nextPart.rBound);
                    break;
                case Minus:
                    nextPart = parse(expression, tokenVal.rightBound + 1,
                            operation.priority);
                    prevPart = new ParsePart(new UnaryMinus(nextPart.expression),
                            nextPart.rBound);
                    leftBound = nextPart.rBound + 1;
                    break;
                case Factorial:
                    if (prevPart == null) {
                        throw new WrongExpressionException();
                    }
                    prevPart = new ParsePart(new Factorial(prevPart.expression), tokenVal.rightBound);
                    break;
                default:
                    throw new WrongExpressionException();
            }
        }
    }

    private static Operation getOperation(Token token, ParsePart prevPart) {
        Operation operation = null;
        for (Operation op : token.operations) {
            if (!op.isUnary && prevPart == null) {
                continue;
            }
            if (op.isUnary && prevPart == null && op.isRight) {
                continue;
            }
            if (op.isUnary && operation != null && !operation.isUnary) {
                continue;
            }
            operation = op;
        }
        // System.err.println("OP> " + operation);
        if (operation == null) {
            throw new WrongExpressionException();
        }
        return operation;
    }
}
