import java.util.ArrayList;
import java.util.List;
import static java.lang.Math.abs;
import static java.lang.Math.max;

public class ReverseMaxAbsModOctDec {
    public static void addBufferToMatrix(List<Integer> buffer, List<int[]> matrix) {
        int[] array = new int[buffer.size()];
        for (int j = 0; j < array.length; j++) {
            array[j] = buffer.get(j);
        }
        matrix.add(array);
        buffer.clear();
    }

    public static void main(String[] args) {
        final int MOD = 1_000_000_007;

        List<int[]> matrix = new ArrayList<>();
        List<Integer> buffer = new ArrayList<>();
        int maxLengthLine = 0;

        try (MyScanner scanner = new MyScanner(System.in)) {
            while (scanner.hasNext()) {
                MyScanner.ValueTwoCount<Integer> intAndLinesCount = scanner.nextIntOrOctIntAndSkipLinesCount();
                if (intAndLinesCount.firstCount > 0) {
                    addBufferToMatrix(buffer, matrix);
                }
                for (int i = 0; i < intAndLinesCount.firstCount - 1; i++) {
                    matrix.add(new int[0]);
                }
                if (intAndLinesCount.element != null) {
                    buffer.add(intAndLinesCount.element);
                }
                if (intAndLinesCount.secondCount > 0) {
                    addBufferToMatrix(buffer, matrix);
                }
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
