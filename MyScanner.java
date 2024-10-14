import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class MyScanner implements Closeable {
    public static class CanNotReadSourceStream extends IOException { }

    public static class ValueTwoCount<T> {
        public final T element;
        public final int firstCount;
        public final int secondCount;

        public ValueTwoCount(T element, int firstCount, int secondCount) {
            this.element = element;
            this.firstCount = firstCount;
            this.secondCount = secondCount;
        }

        @Override
        public String toString() {
            return String.format("(%s, %d, %d)", element, firstCount, secondCount);
        }
    }

    private static final int BUFFER_SIZE = 1024 * 1024 / 2;

    private final char[] buffer;
    private int bufferSize;

    private final Reader source;

    private int bufferPointer;

    private boolean closed = false;

    public MyScanner(InputStream source) throws CanNotReadSourceStream {
        this(source, StandardCharsets.UTF_8);
    }

    public MyScanner(InputStream source, Charset charset) throws CanNotReadSourceStream {
        if (charset == null) {
            charset = StandardCharsets.UTF_8;
        }
        if (source == null) {
            throw new NullPointerException("Input stream is null");
        }

        this.source = new BufferedReader(new InputStreamReader(source, charset), BUFFER_SIZE);
        buffer = new char[BUFFER_SIZE];
        bufferSize = 0;
        bufferPointer = 0;
        updateBuffer();
    }

    private void updateBuffer() throws CanNotReadSourceStream {
        try {
            bufferSize = source.read(buffer);
            bufferPointer = 0;
        } catch (IOException e) {
            throw new CanNotReadSourceStream();
        }
    }

    private void check() {
        checkClosed();
        if (bufferSize < 0) {
            throw new NoSuchElementException("Input stream is exhausted");
        }
    }

    private void checkClosed() {
        if (closed) {
            throw new IllegalStateException("MyScanner is already closed");
        }
    }

    public boolean hasNext() {
        return bufferSize > 0;
    }

    private boolean isLineSeparator(char ch) {
        return System.lineSeparator().indexOf(ch) != -1;
    }

    private boolean isPartOfWord(char ch) {
        return Character.isLetter(ch) || Character.DASH_PUNCTUATION == Character.getType(ch) || ch == '\'';
    }

    private boolean isPartOfNumber(char ch) {
        return Character.isDigit(ch) || ch == '+' || ch == '-' || ch == '.';
    }

    private boolean isPartOfNumberOct(char ch) {
        return isPartOfNumber(ch) || Character.toLowerCase(ch) == 'o';
    }

    public String nextCharSequenceByFunc(Function<Character, Boolean> func) throws CanNotReadSourceStream {
        return nextCharSequenceAndLinesSkipCountByFunc(func).element;
    }

    public ValueTwoCount<String> nextCharSequenceAndLinesSkipCountByFunc(
            Function<Character, Boolean> func) throws CanNotReadSourceStream {
        StringBuilder line = new StringBuilder();
        int lineCountPrev = 0;
        int lineCountAfter = 0;

        char ch = 'q';
        while (hasNext()) {
            ch = nextChar();
            if (isLineSeparator(ch)) {
                for (int i = 0; i < System.lineSeparator().length() - 1 && hasNext(); i++) {
                    nextChar();
                }
                lineCountPrev++;
            }
            if (func.apply(ch)) {
                line.append(ch);
                break;
            }
        }
        boolean flag = false;
        while (hasNext()) {
            ch = nextChar();
            flag = true;
            if (!func.apply(ch)) {
                break;
            }
            line.append(ch);
        }
        if (flag && isLineSeparator(ch)) {
            for (int i = 0; i < System.lineSeparator().length() - 1 && hasNext(); i++) {
                nextChar();
            }
            lineCountAfter++;
        }

        return new ValueTwoCount<>(line.toString(), lineCountPrev, lineCountAfter);
    }

    public String nextWord() throws CanNotReadSourceStream {
        return nextCharSequenceByFunc(this::isPartOfWord);
    }

    public ValueTwoCount<Integer> nextIntAndSkipLinesCount() throws CanNotReadSourceStream {
        ValueTwoCount<String> pair = nextCharSequenceAndLinesSkipCountByFunc(this::isPartOfNumber);
        Integer intPart;
        if (pair.element.isBlank()) {
            intPart = null;
        } else {
            intPart = Integer.parseInt(pair.element);
        }
        return new ValueTwoCount<>(intPart, pair.firstCount, pair.secondCount);
    }

    public ValueTwoCount<Integer> nextIntOrOctIntAndSkipLinesCount() throws CanNotReadSourceStream {
        ValueTwoCount<String> pair = nextCharSequenceAndLinesSkipCountByFunc(this::isPartOfNumberOct);
        String charSequence = pair.element;
        charSequence = charSequence.toLowerCase();

        Integer intPart;
        if (charSequence.isBlank()) {
            intPart = null;
        } else {
            if (charSequence.endsWith("o")) {
                intPart = Integer.parseUnsignedInt(charSequence.substring(0, charSequence.length() - 1), 8);
            } else {
                intPart = Integer.parseInt(charSequence);
            }
        }

        return new ValueTwoCount<>(intPart, pair.firstCount, pair.secondCount);
    }

    public char nextChar() throws CanNotReadSourceStream {
        check();
        char result = buffer[bufferPointer++];
        if (bufferPointer >= bufferSize) {
            updateBuffer();
        }
        return result;
    }

    public String nextLine() throws CanNotReadSourceStream {
        StringBuilder line = new StringBuilder();

        char ch;
        do {
            ch = nextChar();
            line.append(ch);
        } while (!isLineSeparator(ch) && hasNext());

        for (int i = 0; hasNext() && i < System.lineSeparator().length() - 1; i++) {
            line.append(nextChar());
        }

        return line.toString();
    }

    @Override
    public void close() {
        if (closed) {
            return;
        }
        try {
            source.close();
        } catch (IOException ex) {
            System.err.println("Error when try closing input");
        }
        closed = true;
    }
}
