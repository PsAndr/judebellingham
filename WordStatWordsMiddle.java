import scanner.MyScanner;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class WordStatWordsMiddle {
    public static String getMiddleWord(String word) {
        word = word.toLowerCase();
        if (word.length() > 6) {
            return word.substring(3, word.length() - 3);
        }
        return word;
    }

    public static void addWordToMap(Map<String, Integer> map, String word) {
        word = getMiddleWord(word);
        map.merge(word, 1, Integer::sum);
    }

    public static void main(String[] args) {
        final int BUFFER_SIZE = 1024;

        SortedMap<String, Integer> wordsCount = new TreeMap<>();

        try (MyScanner scanner = new MyScanner(new FileInputStream(args[0]))) {
            while (scanner.hasNext()) {
                String word = scanner.nextWord();
                if (word.isEmpty()) {
                    continue;
                }
                addWordToMap(wordsCount, word);
            }
        } catch (MyScanner.CanNotReadSourceStream ex) {
            System.err.println("Error in reading file: " + ex.getMessage());
            return;
        } catch (FileNotFoundException ex) {
            System.err.println("Error in open input file: " + ex.getMessage());
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(args[1]),
                        StandardCharsets.UTF_8
                ),
                BUFFER_SIZE
        )) {
            for (Map.Entry<String, Integer> kvp : wordsCount.reversed().entrySet()) {
                writer.write(String.format("%s %d%n", kvp.getKey(), kvp.getValue()));
            }
        } catch (IOException ex) {
            System.err.println("Error in open or writing in output file: " + ex.getMessage());
        }
    }
}
