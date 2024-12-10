package expression.parser;

import expression.*;

import java.util.List;
import java.util.Objects;

public class ExpressionParser implements TripleParser {
    public static class WrongExpressionException extends RuntimeException {
        public WrongExpressionException(String message) {
            super(message);
        }

        public WrongExpressionException() {
            super();
        }
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

    private record TokenVal(Token token, String value, int rightBound) {
    }

    @Override
    public TripleExpression parse(final String expression) {
        return Objects.requireNonNull(parse(expression, 0, -1)).expression;
    }

    record ParsePart(AllExpression expression, int rBound) {
        @Override
        public String toString() {
            return expression.toMiniString() + "(" + rBound + ")";
        }
    }

    private TokenVal nextToken(final String expression, final int leftBound, final boolean unaryAble) {
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

    private ParsePart parse(final String expression, int leftBound, final int minPriority) {
        ParsePart prevPart = null;
        while (true) {
            TokenVal tokenVal = nextToken(expression, leftBound, prevPart == null);
            if (tokenVal == null) {
                if (prevPart == null) {
                    return null;
                }
                return new ParsePart(prevPart.expression, expression.length());
            }

            leftBound = tokenVal.rightBound + 1;
            Token token = tokenVal.token;
            switch (token) {
                case OPEN_BRACKET:
                    ParsePart inBracket = parse(expression, tokenVal.rightBound + 1, -1);
                    if (inBracket == null) {
                        prevPart = null;
                    } else {
                        prevPart = new ParsePart(inBracket.expression, inBracket.rBound + 1);
                        leftBound = prevPart.rBound + 1;
                    }
                    continue;
                case CLOSE_BRACKET:
                    if (prevPart == null) {
                        throw new WrongExpressionException(String.format("Wrong close bracket (%d)",
                                tokenVal.rightBound));
                    }
                    return new ParsePart(prevPart.expression, tokenVal.rightBound - 1);
                case VARIABLE:
                    prevPart = new ParsePart(new Variable(tokenVal.value), tokenVal.rightBound);
                    continue;
                case CONST:
                    prevPart = new ParsePart(new Const(
                            Integer.parseInt(tokenVal.value)), tokenVal.rightBound);
                    continue;
            }

            final Operation operation = getOperation(token, prevPart);
            if (operation.priority < minPriority) {
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
                    if (prevPart == null || nextPart == null) {
                        throw new WrongExpressionException(String.format("%s (%d)", operation,
                                tokenVal.rightBound));
                    }
                    prevPart = new ParsePart(new Add(prevPart.expression, nextPart.expression),
                            nextPart.rBound);
                    break;
                case Subtract:
                    if (prevPart == null || nextPart == null) {
                        throw new WrongExpressionException(String.format("%s (%d)", operation,
                                tokenVal.rightBound));
                    }
                    prevPart = new ParsePart(new Subtract(prevPart.expression, nextPart.expression),
                            nextPart.rBound);
                    break;
                case Multiply:
                    if (prevPart == null || nextPart == null) {
                        throw new WrongExpressionException(String.format("%s (%d)", operation,
                                tokenVal.rightBound));
                    }
                    prevPart = new ParsePart(new Multiply(prevPart.expression, nextPart.expression),
                            nextPart.rBound);
                    break;
                case Divide:
                    if (prevPart == null || nextPart == null) {
                        throw new WrongExpressionException(String.format("%s (%d)", operation,
                                tokenVal.rightBound));
                    }
                    prevPart = new ParsePart(new Divide(prevPart.expression, nextPart.expression),
                            nextPart.rBound);
                    break;
                case Pow:
                    if (prevPart == null || nextPart == null) {
                        throw new WrongExpressionException(String.format("%s (%d)", operation,
                                tokenVal.rightBound));
                    }
                    prevPart = new ParsePart(new Pow(prevPart.expression, nextPart.expression),
                            nextPart.rBound);
                    break;
                case Log:
                    if (prevPart == null || nextPart == null) {
                        throw new WrongExpressionException(String.format("%s (%d)", operation,
                                tokenVal.rightBound));
                    }
                    prevPart = new ParsePart(new Log(prevPart.expression, nextPart.expression),
                            nextPart.rBound);
                    break;
                case Minus:
                    nextPart = parse(expression, tokenVal.rightBound + 1,
                            operation.priority);
                    if (nextPart == null) {
                        throw new WrongExpressionException(String.format("%s (%d)", operation,
                                tokenVal.rightBound));
                    }
                    prevPart = new ParsePart(new Negative(nextPart.expression),
                            nextPart.rBound);
                    leftBound = nextPart.rBound + 1;
                    break;
                case Factorial:
                    if (prevPart == null) {
                        throw new WrongExpressionException(String.format("%s (%d)", operation,
                                tokenVal.rightBound));
                    }
                    prevPart = new ParsePart(new Factorial(prevPart.expression), tokenVal.rightBound);
                    break;
                default:
                    throw new WrongExpressionException("Unknown operation: " + operation);
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
        if (operation == null) {
            throw new WrongExpressionException();
        }
        return operation;
    }
}
