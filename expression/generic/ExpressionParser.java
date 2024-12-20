package expression.generic;

import java.util.List;
import java.util.Map;
import java.util.Set;

import expression.exceptions.*;

public class ExpressionParser<T extends BaseNumber<T>> implements GenericParser<T> {
    private final ParserNumber<T> parserNumber;

    public ExpressionParser(final ParserNumber<T> parserNumber) {
        this.parserNumber = parserNumber;
    }

    private final Set<String> enableVariablesEnds = Set.of(
            "x", "y", "z"
    );

    private final Map<Character, Character> bracketsMap = Map.of(
            '(', ')',
            '{', '}',
            '[', ']'
    );

    private enum Operation {
        Add(1, "+", false),
        Subtract(1, "-", false),
        Multiply(2, "*", false),
        Mod(2, "mod", false),
        Divide(2, "/", false),
        Pow(4, "**", false),
        Log(4, "//", false),
        Factorial(6, "!", true, true),
        LeftFactorial(7, "!", true),
        Minus(7, "-", true),
        Abs(7, "abs", true),
        Square(7, "square", true),
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
        MOD(List.of(Operation.Mod)),
        DIVIDE(List.of(Operation.Divide)),
        POW(List.of(Operation.Pow)),
        LOG(List.of(Operation.Log)),
        FACTORIAL(List.of(Operation.Factorial, Operation.LeftFactorial)),
        MINUS(List.of(Operation.Minus, Operation.Subtract)),
        ABS(List.of(Operation.Abs)),
        SQUARE(List.of(Operation.Square)),
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
    public AllExpression<T> parse(final String expression) {
        CharSource source = new CharSource(expression);
        AllExpression<T> ans = parse(source, -1);
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
            boolean isFirstLetter = Character.isLetter(c);
            final boolean hasMinus = c == '-';
            StringBuilder sb = new StringBuilder().append(c);
            if (c == '-' || isFirstLetter || Character.isDigit(c)) {
                while (source.hasNext() && ((Character.isLetter(source.getChar()) && isFirstLetter)
                        || Character.isDigit(source.getChar()))) {
                    sb.append(source.nextChar());
                }
                if ((!hasMinus || sb.length() > 1) && !isFirstLetter && unaryAble) {
                    return new TokenVal(Token.CONST, sb.toString());
                }
                if (!isFirstLetter) {
                    source.setStart(source.getStart() - sb.length() + 1);
                    sb.setLength(0);
                    sb.append(c);
                }
            }
            if (bracketsMap.containsKey(c)) {
                return new TokenVal(Token.OPEN_BRACKET, String.valueOf(c));
            }
            if (bracketsMap.containsValue(c)) {
                return new TokenVal(Token.CLOSE_BRACKET, String.valueOf(c));
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
                    if (!operation.isUnary && unaryAble) {
                        continue;
                    }
                    if (operation.stringVal.startsWith(sb.toString())) {
                        if (source.startsWith(operation.stringVal.substring(sb.length()))) {
                            if (ans == null || op.length() < operation.stringVal.length()) {
                                ans = token;
                                op = operation.stringVal;
                            }
                        }
                    }
                }
            }
            if (ans != null) {
                source.setStart(source.getStart() + op.length() - sb.length());
                return new TokenVal(ans, op);
            }
            if (isFirstLetter && !hasMinus) {
                return new TokenVal(Token.VARIABLE, sb.toString());
            }
            throw new TokenException("Unexpected token: " + c + source.toString(4), source.getStart());
        }
        return null;
    }

    private AllExpression<T> parse(final CharSource source, final int minPriority) {
        AllExpression<T> prevPart = null;
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
                    if (!source.hasNext() || source.nextChar() != bracketsMap.get(tokenVal.value.charAt(0))) {
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
                        throw new IncorrectVariableException("Unexpected variable: " + tokenVal.value,
                                source.getStart());
                    }
                    if (tokenVal.value.isEmpty()
                            || !enableVariablesEnds.contains(tokenVal.value.substring(
                                    tokenVal.value.length() - 1))) {
                        throw new IncorrectVariableException("Wrong variable name: " + tokenVal.value,
                                source.getStart());
                    }
                    prevPart = new Variable<T>(tokenVal.value);
                    continue;
                case CONST:
                    if (prevPart != null) {
                        throw new IncorrectVariableException("Unexpected const value: " + tokenVal.value
                                + "(" + prevPart + ")", source.getStart());
                    }
                    try {
                        prevPart = new Const<>(parserNumber.parse(tokenVal.value));
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
            AllExpression<T> nextPart;

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

    private static <T extends BaseNumber<T>> AllExpression<T> makeBinOperation(final Operation op,
                                                     final AllExpression<T> l, final AllExpression<T> r,
                                                     final CharSource source) {
        if (l == null || r == null) {
            throw new OperationException(op + ": wrong left or right operands", source.getStart());
        }
        return switch (op) {
            case Add -> new Add<>(l, r);
            case Subtract -> new Subtract<>(l, r);
            case Multiply -> new Multiply<>(l, r);
            case Divide -> new Divide<>(l, r);
            case Mod -> new Mod<>(l, r);
            default -> throw new OperationException("Unknown bin operation: " + op, source.getStart());
        };
    }

    private static <T extends BaseNumber<T>> AllExpression<T> makeLeftUnaryOperation(final Operation op,
                                                                                     final AllExpression<T> r,
                                                                                     final CharSource source) {
        if (r == null) {
            throw new OperationException(op + ": wrong right operand", source.getStart());
        }
        return switch (op) {
            case Minus -> new Negative<>(r);
            case Abs -> new Abs<>(r);
            case Square -> new Square<>(r);
            default -> throw new OperationException("Unknown left unary operation: " + op, source.getStart());
        };
    }

    private static <T extends BaseNumber<T>> AllExpression<T> makeRightUnaryOperation(final Operation op,
                                                                                      final AllExpression<T> l,
                                                                                      final CharSource source) {
        if (l == null) {
            throw new OperationException(op + ": wrong left operand", source.getStart());
        }
        return switch (op) {
            case Add -> new Add<>(l, l);
            default -> throw new OperationException("Unknown right unary operation: " + op, source.getStart());
        };
    }

    private static <T extends BaseNumber<T>> Operation getOperation(final Token token,
                                                                    final AllExpression<T> prevPart,
                                                                    final CharSource source) {
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
            throw new OperationException(String.format("Can't find optimal operation (%s)", token.operations),
                    source.getStart());
        }
        return operation;
    }
}
