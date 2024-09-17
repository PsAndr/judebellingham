import java.util.Arrays;

public class Test {
    public static class Lol{
        int a, b, c;
        public Lol(int a, int b, int c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        public int[] asArray() {
            return new int[] { a, b, c };
        }

        @Override
        public String toString() {
            return String.format("%d | %d | %d", a, b, c);
        }
    }
    public static void main(String[] args) {
        String hex = "0x1";
        System.out.println(Long.parseUnsignedLong(hex, 16));
    }
}
