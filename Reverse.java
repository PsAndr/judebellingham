import java.io.IOException;
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

    public static void main(String[] args) throws IOException {
        List<int[]> matrix = new ArrayList<>();
        List<Integer> buffer = new ArrayList<>();
        try (MyScanner scanner = new MyScanner(System.in)) {
            while (scanner.hasNext()) {
                Integer val = scanner.nextIntInLine();
                if (val == null) {
                    addBufferToMatrix(buffer, matrix);
                    continue;
                }
                buffer.add(val);
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