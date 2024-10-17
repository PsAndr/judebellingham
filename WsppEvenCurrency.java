import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class WsppEvenCurrency {
    public static class CountAndIndexes {
        private final IntList indexes;
        private int count;

        public CountAndIndexes() {
            indexes = new IntList();
            count = 0;
        }

        public void add(int index) {
            if (index > -1) {
                indexes.add(index);
            }
            count++;
        }
    }

    public static boolean isCharacterCurrency(int ch) {
        return Character.getType(ch) == Character.CURRENCY_SYMBOL;
    }

    public static void main(String[] args) {
        List<String> words = new ArrayList<>();
        Map<String, CountAndIndexes> wordsAndPositions = new HashMap<>();
        Set<String> wordsInLine = new HashSet<>();
        int indexWordInLine = 1;

        try (MyScanner scanner = new MyScanner(new FileInputStream(args[0]))) {
            while (scanner.hasNext()) {
                String word = scanner.nextWordWithOtherSymbolsInLine(WsppEvenCurrency::isCharacterCurrency);
                if (word == null) {
                    indexWordInLine = 1;
                    wordsInLine.clear();
                    continue;
                }
                word = word.toLowerCase();
                CountAndIndexes wordStat = wordsAndPositions.computeIfAbsent(word, k -> new CountAndIndexes());
                if (wordStat.count == 0) {
                    words.add(word);
                }
                if (wordsInLine.contains(word)) {
                    wordStat.add(indexWordInLine);
                    wordsInLine.remove(word);
                } else {
                    wordStat.add(-1);
                    wordsInLine.add(word);
                }
                indexWordInLine++;
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
                CountAndIndexes wordStat = wordsAndPositions.get(word);
                writer.write(String.format("%s %d", word, wordStat.count));
                for (int i = 0; i < wordStat.indexes.size(); i++) {
                    writer.write(' ');
                    writer.write(Integer.toString(wordStat.indexes.get(i)));
                }
                writer.write(System.lineSeparator());
            }
        } catch (IOException ex) {
            System.err.println("Error while working with output file: " + ex.getMessage());
        }
    }
}
