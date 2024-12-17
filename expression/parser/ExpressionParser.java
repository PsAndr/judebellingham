package expression.parser;

import expression.*;

import java.util.List;

public class ExpressionParser implements TripleParser {
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

    private record TokenVal(Token token, String value) {
    }

    @Override
    public TripleExpression parse(final String expression) {
        return parse(new CharSource(expression), -1);
    }

    private TokenVal nextToken(final CharSource source, final boolean unaryAble) {
        while (source.hasNext()) {
            char c = source.nextChar();
            if (Character.isWhitespace(c)) {
                continue;
            }
            if (Character.isDigit(c) ||
                    (c == '-' && source.hasNext() && Character.isDigit(source.getChar())
                            && unaryAble)) {
                StringBuilder sb = new StringBuilder().append(c);
                while (source.hasNext() && Character.isDigit(source.getChar())) {
                    sb.append(source.nextChar());
                }
                return new TokenVal(Token.CONST, sb.toString());
            }
            if (c == '(') {
                return new TokenVal(Token.OPEN_BRACKET, "(");
            }
            if (c == ')') {
                return new TokenVal(Token.CLOSE_BRACKET, ")");
            }
            if (Character.isLetter(c)) {
                StringBuilder sb = new StringBuilder().append(c);
                while (source.hasNext() && Character.isLetter(source.getChar())) {
                    sb.append(source.nextChar());
                }
                return new TokenVal(Token.VARIABLE, sb.toString());
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
                        if (source.startsWith(operation.stringVal.substring(1))) {
                            if (ans == null || op.length() < operation.stringVal.length()) {
                                ans = token;
                                op = operation.stringVal;
                            }
                        }
                    }
                }
            }
            if (!op.isEmpty()) {
                source.setStart(source.getStart() + op.length() - 1);
            }
            return new TokenVal(ans, op);
        }
        return null;
    }

    private AllExpression parse(final CharSource source, final int minPriority) {
        AllExpression prevPart = null;
        while (true) {
            int startBeforeToken = source.getStart();
            TokenVal tokenVal = nextToken(source, prevPart == null);
            if (tokenVal == null) {
                return prevPart;
            }

            Token token = tokenVal.token;
            switch (token) {
                case OPEN_BRACKET:
                    prevPart = parse(source, -1);
                    if (source.nextChar() != ')') {
                        throw new WrongExpressionException("No close bracket");
                    }
                    continue;
                case CLOSE_BRACKET:
                    if (prevPart == null) {
                        throw new WrongExpressionException("Wrong close bracket");
                    }
                    source.setStart(source.getStart() - 1);
                    return prevPart;
                case VARIABLE:
                    prevPart = new Variable(tokenVal.value);
                    continue;
                case CONST:
                    prevPart = new Const(Integer.parseInt(tokenVal.value));
                    continue;
            }

            final Operation operation = getOperation(token, prevPart);

            if (operation.priority < minPriority) {
                source.setStart(startBeforeToken);
                return prevPart;
            }
            AllExpression nextPart = null;

            if (!operation.isUnary) {
                nextPart = parse(source, operation.priority + 1);
                prevPart = makeBinOperation(operation, prevPart, nextPart);
            } else if (operation.isRight) {
                prevPart = makeRightUnaryOperation(operation, prevPart);
            } else {
                nextPart = parse(source, operation.priority);
                prevPart = makeLeftUnaryOperation(operation, nextPart);
            }
        }
    }

    private static AllExpression makeBinOperation(Operation op, AllExpression l, AllExpression r) {
        if (l == null || r == null) {
            throw new WrongExpressionException(op.toString());
        }
        return switch (op) {
            case Add -> new Add(l, r);
            case Subtract -> new Subtract(l, r);
            case Pow -> new Pow(l, r);
            case Multiply -> new Multiply(l, r);
            case Divide -> new Divide(l, r);
            case Log -> new Log(l, r);
            default -> throw new WrongExpressionException("Unknown bin operation: " + op);
        };
    }

    private static AllExpression makeLeftUnaryOperation(Operation op, AllExpression r) {
        if (r == null) {
            throw new WrongExpressionException(op.toString());
        }
        return switch (op) {
            case Minus -> new Negative(r);
            default -> throw new WrongExpressionException("Unknown left unary operation: " + op);
        };
    }

    private static AllExpression makeRightUnaryOperation(Operation op, AllExpression l) {
        if (l == null) {
            throw new WrongExpressionException(op.toString());
        }
        return switch (op) {
            case Factorial -> new Factorial(l);
            default -> throw new WrongExpressionException("Unknown right unary operation: " + op);
        };
    }

    private static Operation getOperation(Token token, AllExpression prevPart) {
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
