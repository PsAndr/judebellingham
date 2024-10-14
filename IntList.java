import java.util.*;


public class IntList implements Iterable<Integer> {
    private int[] array;
    private int size;

    public IntList() {
        array = new int[4];
        size = 0;
    }

    public int size() {
        return size;
    }

    public int get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return array[index];
    }

    public int set(int index, int value) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        int prevVal = array[index];
        array[index] = value;
        return prevVal;
    }

    private void addMemory() {
        int[] newArray = new int[array.length + array.length / 2];
        System.arraycopy(array, 0, newArray, 0, array.length);
        array = newArray;
    }

    public void add(int val) {
        if (size == array.length) {
            addMemory();
        }
        array[size++] = val;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        size = 0;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new IntListIterator();
    }

    public class IntListIterator implements Iterator<Integer> {
        private int index;

        private IntListIterator() {
            index = 0;
        }

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public Integer next() {
            return array[index++];
        }
    }
}
