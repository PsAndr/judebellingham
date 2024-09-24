import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReverseSumAbsMod {
    public static int getCountNumbers(String s) {
        int cnt = 0;
        int lastIndex = -1;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);

            if (!Character.isDigit(ch) && ch != '-') {
                if (i - lastIndex > 1) {
                    cnt++;
                }
                lastIndex = i;
            }
        }
        if (s.length() - lastIndex > 1) {
            cnt++;
        }

        return cnt;
    }

    public static void main(String[] args) {
        final int mod = 1_000_000_007;

        Scanner scanner = new Scanner(System.in);

        List<int[]> array = new ArrayList<>();
        List<Integer> buffer = new ArrayList<>();

        int maxLengthLine = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            int lastIndex = -1;
            for (int i = 0; i < line.length(); i++) {
                char ch = line.charAt(i);

                if (!Character.isDigit(ch) && ch != '-') {
                    if (i - lastIndex > 1) {
                        buffer.add(Integer.parseInt(line.substring(lastIndex + 1, i)));
                    }
                    lastIndex = i;
                }
            }
            if (line.length() - lastIndex > 1) {
                buffer.add(Integer.parseInt(line.substring(lastIndex + 1)));
            }
            array.add(new int[buffer.size()]);
            for (int i = 0; i < buffer.size(); i++) {
                array.getLast()[i] = buffer.get(i);
            }

            maxLengthLine = Math.max(maxLengthLine, buffer.size());
            buffer.clear();
        }

        int[] sumLines = new int[array.size()];
        int[] sumColumns = new int[maxLengthLine];

        for (int i = 0; i < array.size(); i++) {
            for (int j = 0; j < array.get(i).length; j++) {
                sumLines[i] += Math.abs(array.get(i)[j]) % mod;
                sumLines[i] %= mod;
                sumColumns[j] += Math.abs(array.get(i)[j]) % mod;
                sumColumns[j] %= mod;
            }
        }

        for (int i = 0; i < array.size(); i++) {
            for (int j = 0; j < array.get(i).length; j++) {
                System.out.printf("%d ", ((sumColumns[j] + sumLines[i]) % mod -
                        Math.abs(array.get(i)[j]) % mod + mod) % mod);
            }
            System.out.println();
        }
    }
}