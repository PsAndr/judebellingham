import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Scanner;

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
    public static void main(String[] args)  {
        int[] arr = new int[10000];
        // System.out.println(Arrays.toString(arr));
        while (true) {

        }
    }
}
