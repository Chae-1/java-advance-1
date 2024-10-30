package thread.collection.simple;

import static util.ThreadUtils.sleep;

import java.util.Arrays;

public class BasicList implements SimpleList {

    private static final int DEFAULT_CAPACITY = 10;

    private Object[] elements;
    private int size;

    public BasicList() {
        this.elements = new Object[DEFAULT_CAPACITY];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(Object e) {
        elements[size] = e;
        sleep(100);
        size++;
    }

    @Override
    public Object get(int index) {
        return elements[index];
    }

    @Override
    public String toString() {
        return Arrays.toString(Arrays.copyOf(elements, size)) + " size=" + size + ", capacity=" + elements.length;
    }
}
