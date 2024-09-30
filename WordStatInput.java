import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class WordStatInput {
    public static class MyContainer {
        public static class ContainerElement {
            private int count;
            private final String word;

            public ContainerElement(String word) {
                this.word = word;
                count = 1;
            }

            public String getWord() {
                return word;
            }

            public int getCount() {
                return count;
            }

            public void addCount() {
                count++;
            }
        }

        private final List<ContainerElement> elements;

        public MyContainer() {
            elements = new ArrayList<>();
        }

        public void addWord(String word) {
            for (ContainerElement element : elements) {
                if (Objects.equals(element.getWord(), word)) {
                    element.addCount();
                    return;
                }
            }
            elements.add(new ContainerElement(word));
        }

        public int size() {
            return elements.size();
        }

        public String getWordAt(int index) {
            return elements.get(index).getWord();
        }

        public int getCountAt(int index) {
            return elements.get(index).getCount();
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
            for (int i = 0; i < container.size(); i++) {
                fileWriter.write(container.getWordAt(i));
                fileWriter.write(' ');
                fileWriter.write(String.valueOf(container.getCountAt(i)));
                fileWriter.write('\n');
            }
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }
}
