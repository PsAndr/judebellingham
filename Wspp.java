import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Wspp {
    public static void main(String[] args) {
        List<String> words = new ArrayList<>();
        Map<String, IntList> wordsAndPositions = new HashMap<>();
        int indexWord = 1;

        try (MyScanner scanner = new MyScanner(new FileInputStream(args[0]))) {
            while (scanner.hasNext()) {
                String word = scanner.nextWord();
                if (word.isBlank()) {
                    continue;
                }
                word = word.toLowerCase();
                IntList positions = wordsAndPositions.computeIfAbsent(word, k -> new IntList());
                if (positions.isEmpty()) {
                    words.add(word);
                }
                positions.add(indexWord++);
            }
        } catch (MyScanner.CanNotReadSourceStream ex) {
            System.err.println("Error while reading file: " + ex.getMessage());
        } catch (IOException ex) {
            System.err.println("Error while opening file: " + ex.getMessage());
        }
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(args[1]),
                        StandardCharsets.UTF_8
                ),
                1024 * 1024
        )) {
            for (String word : words) {
                IntList indexes = wordsAndPositions.get(word);
                writer.write(String.format("%s %d ", word, indexes.size()));
                for (int i = 0; i < indexes.size(); i++) {
                    if (i > 0) {
                        writer.write(' ');
                    }
                    writer.write(Integer.toString(indexes.get(i)));
                }
                writer.write(System.lineSeparator());
            }
        } catch (IOException ex) {
            System.err.println("Error while working with output file: " + ex.getMessage());
        }
    }
}
