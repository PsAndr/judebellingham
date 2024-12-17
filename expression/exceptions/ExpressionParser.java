package expression.exceptions;

import expression.*;

import java.util.List;
import java.util.Set;

public class ExpressionParser implements TripleParser {
    private final Set<String> enableVariablesEnds = Set.of(
            "x", "y", "z"
    );

    private enum Operation {
        Add(1, "+", false),
        Subtract(1, "-", false),
        Multiply(2, "*", false),
        Divide(2, "/", false),
        Pow(4, "**", false),
        Log(4, "//", false),
        Factorial(6, "!", true, true),
        LeftFactorial(7, "!", true),
        Minus(7, "-", true),
        GCD(0, "gcd", false),
        LCM(0, "lcm", false),
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
        FACTORIAL(List.of(Operation.Factorial, Operation.LeftFactorial)),
        MINUS(List.of(Operation.Minus, Operation.Subtract)),
        GCD(List.of(Operation.GCD)),
        LCM(List.of(Operation.LCM)),

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
        CharSource source = new CharSource(expression);
        TripleExpression ans = parse(source, -1);
        if (source.hasNext()) {
            throw new WrongExpressionException("Unexpected end expression", source.getStart());
        }
        return ans;
    }

    private TokenVal nextToken(final CharSource source, final boolean unaryAble) {
        while (source.hasNext()) {
            char c = source.nextChar();
            if (Character.isWhitespace(c)) {
                continue;
            }
            if (c != '-' || !(source.hasNext() && Character.isDigit(source.getChar())) || !unaryAble) {
                if (c == '(') {
                    return new TokenVal(Token.OPEN_BRACKET, "(");
                }
                if (c == ')') {
                    return new TokenVal(Token.CLOSE_BRACKET, ")");
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
                if (ans != null) {
                    source.setStart(source.getStart() + op.length() - 1);
                    return new TokenVal(ans, op);
                }
            }
            if (c == '-' || Character.isLetter(c) || Character.isDigit(c)) {
                final boolean hasMinus = c == '-';
                boolean hasLetter = Character.isLetter(c);
                StringBuilder sb = new StringBuilder().append(c);
                while (source.hasNext() && (Character.isLetter(source.getChar())
                        || Character.isDigit(source.getChar()))) {
                    if (Character.isLetter(source.getChar())) {
                        hasLetter = true;
                    }
                    sb.append(source.nextChar());
                }
                if (hasLetter && !hasMinus) {
                    return new TokenVal(Token.VARIABLE, sb.toString());
                } else if (!hasLetter) {
                    return new TokenVal(Token.CONST, sb.toString());
                }
            }
            throw new TokenException("Unexpected token: " + c + source.toString(4), source.getStart());
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
                    if (!source.hasNext() || source.nextChar() != ')') {
                        throw new BracketParseException("No close bracket", source.getStart());
                    }
                    continue;
                case CLOSE_BRACKET:
                    if (prevPart == null) {
                        throw new BracketParseException("Wrong close bracket", source.getStart());
                    }
                    source.setStart(source.getStart() - 1);
                    return prevPart;
                case VARIABLE:
                    if (prevPart != null) {
                        throw new IncorrectVariableException("Unexpected variable", source.getStart());
                    }
                    if (tokenVal.value.isEmpty()
                            || !enableVariablesEnds.contains(tokenVal.value.substring(
                                    tokenVal.value.length() - 1))) {
                        throw new IncorrectVariableException("Wrong variable name: " + tokenVal.value,
                                source.getStart());
                    }
                    prevPart = new Variable(tokenVal.value);
                    continue;
                case CONST:
                    if (prevPart != null) {
                        throw new IncorrectVariableException("Unexpected const value: " + tokenVal.value
                                + "(" + prevPart + ")", source.getStart());
                    }
                    try {
                        prevPart = new Const(Integer.parseInt(tokenVal.value));
                    } catch (NumberFormatException e) {
                        throw new ConstantParseException("Wrong const value: " + tokenVal.value, source.getStart());
                    }
                    continue;
            }

            final Operation operation = getOperation(token, prevPart, source);

            if (operation.priority < minPriority) {
                source.setStart(startBeforeToken);
                return prevPart;
            }
            AllExpression nextPart = null;

            if (!operation.isUnary) {
                nextPart = parse(source, operation.priority + 1);
                prevPart = makeBinOperation(operation, prevPart, nextPart, source);
            } else if (operation.isRight) {
                prevPart = makeRightUnaryOperation(operation, prevPart, source);
            } else {
                nextPart = parse(source, operation.priority);
                prevPart = makeLeftUnaryOperation(operation, nextPart, source);
            }
        }
    }

    private static AllExpression makeBinOperation(final Operation op, final AllExpression l, final AllExpression r,
                                                  final CharSource source) {
        if (l == null || r == null) {
            throw new OperationException(op + ": wrong left or right operands", source.getStart());
        }
        return switch (op) {
            case Add -> new CheckedAdd(l, r);
            case Subtract -> new CheckedSubtract(l, r);
            case Pow -> new Pow(l, r);
            case Multiply -> new CheckedMultiply(l, r);
            case Divide -> new CheckedDivide(l, r);
            case Log -> new Log(l, r);
            case GCD -> new Gcd(l, r);
            case LCM -> new Lcm(l, r);
            default -> throw new OperationException("Unknown bin operation: " + op, source.getStart());
        };
    }

    private static AllExpression makeLeftUnaryOperation(final Operation op, final AllExpression r,
                                                        final CharSource source) {
        if (r == null) {
            throw new OperationException(op + ": wrong right operand", source.getStart());
        }
        return switch (op) {
            case Minus -> new CheckedNegate(r);
            case LeftFactorial -> new LeftFactorial(r);
            default -> throw new OperationException("Unknown left unary operation: " + op, source.getStart());
        };
    }

    private static AllExpression makeRightUnaryOperation(final Operation op, final AllExpression l,
                                                         final CharSource source) {
        if (l == null) {
            throw new OperationException(op + ": wrong left operand", source.getStart());
        }
        return switch (op) {
            case Factorial -> new Factorial(l);
            default -> throw new OperationException("Unknown right unary operation: " + op, source.getStart());
        };
    }

    private static Operation getOperation(final Token token, final AllExpression prevPart, final CharSource source) {
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
            throw new OperationException("Operation unexpected (null)", source.getStart());
        }
        return operation;
    }
}
