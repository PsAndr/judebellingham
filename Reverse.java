import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Reverse {
    public static void main(String[] args) {
        FileReader file;
        try {
            file = new FileReader("txt.txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Scanner scanner = new Scanner(System.in);
        // Scanner scanner = new Scanner(file);
        ArrayList<Integer> array = new ArrayList<>();
        array.add(null);

        String line;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            int lastIndex = -1;
            for (int i = 0; i < line.length(); i++) {
                char ch = line.charAt(i);

                if (!Character.isDigit(ch) && ch != '-') {
                    if (i - lastIndex > 1) {
                        array.add(Integer.parseInt(line.substring(lastIndex + 1, i)));
                    }
                    lastIndex = i;
                }
            }
            if (line.length() - lastIndex > 1) {
                array.add(Integer.parseInt(line.substring(lastIndex + 1)));
            }
            array.add(null);
        }

        for (Integer el : array.reversed().subList(1, array.size())) {
            if (el != null) {
                System.out.print(el);
                System.out.print(' ');
            } else {
                System.out.print('\n');
            }
        }
        System.err.println(array.size());
        System.err.println(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
        System.err.println(1_000_000 * 4 * 8);
    }
}