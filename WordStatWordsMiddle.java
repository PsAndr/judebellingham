import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class WordStatWordsMiddle {
    public static String getMiddleWord(String line, int indStartWord, int indEndWord) {
        if (indEndWord - indStartWord > 6) {
            indStartWord += 3;
            indEndWord -= 3;
        }
        return line.substring(indStartWord, indEndWord).toLowerCase();
    }

    public static void main(String[] args) {
        List<String> allWords = new ArrayList<>();

        BufferedReader reader;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(args[0]),
                            StandardCharsets.UTF_8
                    ),
                    1024
            );
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    int lastInd = -1;
                    for (int i = 0; i < line.length(); i++) {
                        char ch = line.charAt(i);
                        int chType = Character.getType(ch);
                        if (!Character.isLetter(ch) && Character.DASH_PUNCTUATION != chType &&
                                ch != '\'') {
                            if (i - lastInd > 1) {
                                allWords.add(getMiddleWord(line, lastInd + 1, i));
                            }
                            lastInd = i;
                        }
                    }
                    if (line.length() - lastInd > 1) {
                        allWords.add(getMiddleWord(line, lastInd + 1, line.length()));
                    }
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

        String[] allWordsArr = new String[allWords.size()];
        allWordsArr = allWords.toArray(allWordsArr);

        Arrays.sort(allWordsArr);

        try (FileWriter fileWriter = new FileWriter(args[1])) {
            String lastWord = null;
            int count = 0;
            for (int i = allWordsArr.length - 1; i >= 0; i--) {
                String word = allWordsArr[i];
                if (word.equals(lastWord)) {
                    count++;
                } else {
                    if (count > 0) {
                        fileWriter.write(String.format("%s %d\n", lastWord, count));
                    }
                    count = 1;
                    lastWord = word;
                }
            }
            if (count > 0) {
                fileWriter.write(String.format("%s %d\n", lastWord, count));
            }
        } catch (IOException ex) {
            System.err.println("Error in open or writing in output file");
            ex.printStackTrace(System.err);
        }
    }
}
