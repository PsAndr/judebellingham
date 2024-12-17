package expression.exceptions;

public class CharSource {
    private final String source;
    private int start;

    public CharSource(String source, int start) {
        this.source = source;
        this.start = start;
    }

    public CharSource(String source) {
        this(source, 0);
    }

    public CharSource(CharSource source) {
        this(source.source, 0);
    }

    public CharSource(CharSource source, int start) {
        this(source.source, start);
    }

    public boolean hasNext() {
        return start < source.length();
    }

    public char nextChar() {
        char c = getChar();
        start++;
        return c;
    }

    public char getChar() {
        return source.charAt(start);
    }

    public int getStart() {
        return start;
    }

    public int setStart(int start) {
        this.start = Math.max(0, Math.min(source.length(), start));
        return this.start;
    }

    public boolean startsWith(final String prefix) {
        if (start + prefix.length() > source.length()) {
            return false;
        }
        for (int i = start; i < start + prefix.length(); i++) {
            if (source.charAt(i) != prefix.charAt(i - start)) {
                return false;
            }
        }
        return true;
    }

    public String toString(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < start + count; i++) {
            if (i >= source.length()) {
                break;
            }
            sb.append(source.charAt(i));
        }
        if (start + count < source.length()) {
            sb.append("...");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return source.substring(start);
    }
}
