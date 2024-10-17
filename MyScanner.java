import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.function.IntPredicate;

public class MyScanner implements Closeable {
    public static class CanNotReadSourceStream extends IOException { }

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

    private boolean isPartOfWord(int ch) {
        return Character.isLetter(ch) || Character.DASH_PUNCTUATION == Character.getType(ch) || ch == '\'';
    }

    private boolean isPartOfNumber(int ch) {
        return Character.isDigit(ch) || ch == '+' || ch == '-' || ch == '.';
    }

    private boolean isPartOfNumberOct(int ch) {
        return isPartOfNumber(ch) || Character.toLowerCase(ch) == 'o';
    }

    public String nextCharSequenceInLineByFunc(IntPredicate func) throws CanNotReadSourceStream {
        StringBuilder line = new StringBuilder();
        char ch;
        while (hasNext()) {
            ch = getChar();
            if (ch == System.lineSeparator().charAt(0)) {
                boolean flag = true;
                for (int i = 0; i < System.lineSeparator().length() && hasNext(); i++) {
                    ch = getChar();
                    if (ch != System.lineSeparator().charAt(i)) {
                        flag = false;
                        break;
                    }
                    nextChar();
                }
                if (flag) {
                    return null;
                }
            }
            if (func.test(ch)) {
                break;
            }
            nextChar();
        }
        while (hasNext()) {
            ch = getChar();
            if (!func.test(ch)) {
                break;
            }
            line.append(ch);
            nextChar();
        }

        if (line.isEmpty()) {
            return null;
        }

        return line.toString();
    }

    public String nextWord() throws CanNotReadSourceStream {
        return nextCharSequenceInLineByFunc(this::isPartOfWord);
    }

    public String nextWordAndSkipLinesCount() throws CanNotReadSourceStream {
        return nextCharSequenceInLineByFunc(this::isPartOfWord);
    }

    public String nextWordWithOtherSymbolsInLine(IntPredicate otherSymbolsCheck)
            throws CanNotReadSourceStream {
        return nextCharSequenceInLineByFunc((int ch) ->
                isPartOfWord(ch) || otherSymbolsCheck.test(ch));
    }

    public Integer nextIntInLine() throws CanNotReadSourceStream {
        String s = nextCharSequenceInLineByFunc(this::isPartOfNumber);
        if (s == null) {
            return null;
        }
        return Integer.parseInt(s);
    }

    // remove count
    public Integer nextIntOrOctIntInLine() throws CanNotReadSourceStream {
        String s = nextCharSequenceInLineByFunc(this::isPartOfNumberOct);
        if (s == null) {
            return null;
        }

        s = s.toLowerCase();
        int intPart;
        if (s.endsWith("o")) {
            intPart = Integer.parseUnsignedInt(s.substring(0, s.length() - 1), 8);
        } else {
            intPart = Integer.parseInt(s);
        }

        return intPart;
    }

    public char getChar() {
        check();
        return buffer[bufferPointer];
    }

    public char nextChar() throws CanNotReadSourceStream {
        check();
        char result = buffer[bufferPointer++];
        if (bufferPointer >= bufferSize) {
            updateBuffer();
        }
        return result;
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
