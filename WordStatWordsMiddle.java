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
        map.putIfAbsent(word, 0);
        map.merge(word, 1, Integer::sum);
    }

    public static void main(String[] args) {
        final int BUFFER_SIZE = 1024;

        Map<String, Integer> wordsCount = new TreeMap<>();

        BufferedReader reader;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(args[0]),
                            StandardCharsets.UTF_8
                    ),
                    BUFFER_SIZE
            );
            try {
                char[] buffer = new char[BUFFER_SIZE];
                StringBuilder prevPartWord = new StringBuilder();
                int lenBuffer;
                while ((lenBuffer = reader.read(buffer)) > -1) {
                    int lastInd = -1;
                    for (int i = 0; i < lenBuffer; i++) {
                        char ch = buffer[i];
                        int chType = Character.getType(ch);
                        if (!Character.isLetter(ch) && Character.DASH_PUNCTUATION != chType && ch != '\'') {
                            if (i - lastInd + prevPartWord.length() > 1) {
                                String word = new String(Arrays.copyOfRange(buffer, lastInd + 1, i));
                                if (!prevPartWord.isEmpty()) {
                                    word = prevPartWord + word;
                                    prevPartWord = new StringBuilder();
                                }
                                addWordToMap(wordsCount, word);
                            }
                            lastInd = i;
                        }
                    }
                    if (lenBuffer - lastInd > 1) {
                        prevPartWord.append(new String(Arrays.copyOfRange(buffer, lastInd + 1, lenBuffer)));
                    }
                }
                if (!prevPartWord.isEmpty()) {
                    addWordToMap(wordsCount, prevPartWord.toString());
                }
            } catch (IOException ex) {
                System.err.println("Error in reading input file");
                ex.printStackTrace(System.err);
                return;
            } finally {
                reader.close();
            }
        } catch (IOException ex) {
            System.err.println("Error in open input file");
            ex.printStackTrace(System.err);
            return;
        }

        String[] sortedWords = new String[wordsCount.size()];
        sortedWords = wordsCount.keySet().toArray(sortedWords);

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(args[1]),
                        StandardCharsets.UTF_8
                ),
                BUFFER_SIZE
        )) {
            for (int i = sortedWords.length - 1; i >= 0; i--) {
                String word = sortedWords[i];
                writer.write(String.format("%s %d\n", word, wordsCount.get(word)));
            }
        } catch (IOException ex) {
            System.err.println("Error in open or writing in output file");
            ex.printStackTrace(System.err);
        }
    }
}
