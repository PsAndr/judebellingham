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

        public static class MyArrayList implements Iterable<ContainerElement> {
            private ContainerElement[] array;
            private int capacity;
            private int size;

            public MyArrayList() {
                size = 0;
                capacity = 2;
                array = new ContainerElement[2];
            }

            public int size() {
                return size;
            }

            public void add(ContainerElement newElement) {
                array[size++] = newElement;
                if (size == capacity) {
                    capacity *= 2;
                    ContainerElement[] tempArray = new ContainerElement[capacity];
                    System.arraycopy(array, 0, tempArray, 0, size);
                    array = tempArray;
                }
            }

            public ContainerElement get(int index) {
                if (index >= 0 && index < size) {
                    return array[index];
                }
                throw new IndexOutOfBoundsException();
            }

            @Override
            public Iterator<ContainerElement> iterator() {
                return new IteratorMyArrayList();
            }

            class IteratorMyArrayList implements Iterator<ContainerElement> {

                private int index = 0;

                public boolean hasNext() {
                    return index < size();
                }

                public ContainerElement next() {
                    return get(index++);
                }

                public void remove() {
                    throw new UnsupportedOperationException("not supported yet");
                }
            }
        }

        private final MyArrayList elements;

        public MyContainer() {
            elements = new MyArrayList();
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
                    // System.err.println(line);
                    int lastInd = -1;
                    for (int i = 0; i < line.length(); i++) {
                        char ch = line.charAt(i);
                        int chType = Character.getType(ch);
                        if (!Character.isLetter(ch) && Character.DASH_PUNCTUATION != chType &&
                                ch != '\'') {
                            if (i - lastInd > 1) {
                                String word = line.substring(lastInd + 1, i).toLowerCase();
                                // System.err.println(word);
                                container.addWord(word);
                            }
                            lastInd = i;
                        }
                    }
                    if (line.length() - lastInd > 1) {
                        String word = line.substring(lastInd + 1).toLowerCase();
                        // System.err.println(word);
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
