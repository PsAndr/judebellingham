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
        Lol lol = new Lol(4, 5, 2);
        System.out.println(lol);
        int[] arr = new int[3];
        System.arraycopy(lol.asArray(), 0, arr, 0, 3);
        System.out.println(Arrays.toString(arr));
    }
}
