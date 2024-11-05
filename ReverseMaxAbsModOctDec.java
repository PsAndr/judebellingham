import scanner.MyScanner;

import java.util.ArrayList;
import java.util.List;
import static java.lang.Math.abs;
import static java.lang.Math.max;

public class ReverseMaxAbsModOctDec {
    public static void addBufferToMatrix(List<Integer> buffer, List<int[]> matrix) {
        Reverse.addBufferToMatrix(buffer, matrix);
    }

    public static void main(String[] args) {
        final int MOD = 1_000_000_007;

        List<int[]> matrix = new ArrayList<>();
        List<Integer> buffer = new ArrayList<>();
        int maxLengthLine = 0;

        try (MyScanner scanner = new MyScanner(System.in)) {
            while (scanner.hasNext()) {
                Integer val = scanner.nextIntOrOctIntInLine();
                if (val == null) {
                    addBufferToMatrix(buffer, matrix);
                    continue;
                }
                buffer.add(val);
            }
        } catch (MyScanner.CanNotReadSourceStream ex) {
            System.err.println("Error while reading file: " + ex.getMessage());
        }

        for (int[] line : matrix) {
            maxLengthLine = max(maxLengthLine, line.length);
        }
        int[] maxLines = new int[matrix.size()];
        int[] maxColumns = new int[maxLengthLine];

        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.get(i).length; j++) {
                if (j == 0) {
                    maxLines[i] = matrix.get(i)[j];
                }
                if (i == 0) {
                    maxColumns[j] = matrix.get(i)[j];
                }
                if (abs(maxLines[i]) % MOD < abs(matrix.get(i)[j]) % MOD) {
                    maxLines[i] = matrix.get(i)[j];
                }
                if (abs(maxColumns[j]) % MOD < abs(matrix.get(i)[j]) % MOD) {
                    maxColumns[j] = matrix.get(i)[j];
                }
            }
        }

        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.get(i).length; j++) {
                int el = maxColumns[j];
                if (abs(el) % MOD < abs(maxLines[i]) % MOD) {
                    el = maxLines[i];
                }
                System.out.printf("%oo ", el);
            }
            System.out.println();
        }
    }
}
