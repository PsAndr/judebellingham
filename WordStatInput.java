import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class WordStatInput {
    public static class MyContainer {
        private final Map<String, Integer> elements;
        private final List<String> words;

        public MyContainer() {
            elements = new HashMap<>();
            words = new ArrayList<>();
        }

        public void addWord(String word) {
            if (!elements.containsKey(word)) {
                elements.put(word, 0);
                words.add(word);
            }
            elements.replace(word, elements.get(word) + 1);
        }

        public List<String> getWords() {
            return words;
        }

        public int getCount(String word) {
            return elements.get(word);
        }
    }

    public static void main(String[] args) {
        MyContainer container = new MyContainer();
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
                                String word = line.substring(lastInd + 1, i).toLowerCase();
                                container.addWord(word);
                            }
                            lastInd = i;
                        }
                    }
                    if (line.length() - lastInd > 1) {
                        String word = line.substring(lastInd + 1).toLowerCase();
                        container.addWord(word);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
                return;
            } finally {
                reader.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            return;
        }

        try (FileWriter fileWriter = new FileWriter(args[1])) {
            for (String word : container.getWords()) {
                fileWriter.write(word);
                fileWriter.write(' ');
                fileWriter.write(String.valueOf(container.getCount(word)));
                fileWriter.write('\n');
            }
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }
}
