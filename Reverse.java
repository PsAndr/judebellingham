import java.util.ArrayList;
import java.util.List;

public class Reverse {
    public static void addBufferToMatrix(List<Integer> buffer, List<int[]> matrix) {
        int[] array = new int[buffer.size()];
        for (int j = 0; j < array.length; j++) {
            array[j] = buffer.get(j);
        }
        matrix.add(array);
        buffer.clear();
    }

    public static void main(String[] args) {
        List<int[]> matrix = new ArrayList<>();
        List<Integer> buffer = new ArrayList<>();

        try (MyScanner scanner = new MyScanner(System.in)) {
            while (scanner.hasNext()) {
                MyScanner.ValueTwoCount<Integer> intAndLinesCount = scanner.nextIntAndSkipLinesCount();
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

        for (int[] lineArr : matrix.reversed()) {
            for (int i = lineArr.length - 1; i >= 0; i--) {
                System.out.print(lineArr[i]);
                System.out.print(' ');
            }
            System.out.print(System.lineSeparator());
        }
    }
}