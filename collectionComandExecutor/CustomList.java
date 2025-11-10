package collectionComandExecutor;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CustomList<T> implements Iterable<T> {
    private Object[] elements;
    private int size;

    public CustomList() {
        this.elements = new Object[10];
        this.size = 0;
    }

    public CustomList(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive: " + initialCapacity);
        } else {
            this.elements = new Object[initialCapacity];
            this.size = 0;
        }
    }

    private void ensureCapacity() {
        if (this.size == this.elements.length) {
            int newCapacity = this.elements.length + (this.elements.length >> 1) + 1;
            Object[] newArray = new Object[newCapacity];
            this.elements = this.manualArrayCopy(this.elements, newCapacity);
        }

    }

    private Object[] manualArrayCopy(Object[] original, int newLength) {
        Object[] newArray = new Object[newLength];
        int copyLength = Math.min(original.length, newLength);

        for (int i = 0; i < copyLength; ++i) {
            newArray[i] = original[i];
        }

        return newArray;
    }

    public void add(T element) {
        this.ensureCapacity();
        this.elements[this.size] = element;
        ++this.size;
    }

    public T get(int index) {
        this.checkIndex(index);
        return (T)this.elements[index];
    }

    public void set(int index, T element) {
        this.checkIndex(index);
        this.elements[index] = element;
    }

    public int size() {
        return this.size;
    }

    public void clear() {
        for(int i = 0; i < this.size; ++i) {
            this.elements[i] = null;
        }

        this.size = 0;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException(String.format("Index %d is out of bounds for size %d", index, this.size));
        }
    }

    public T remove(int index) {
        this.checkIndex(index);
        T removedElement = (T)this.elements[index];

        for(int i = index; i < this.size - 1; ++i) {
            this.elements[i] = this.elements[i + 1];
        }

        this.elements[this.size] = null;
        --this.size;
        return removedElement;
    }

    public boolean contains(T element) {
        for(int i = 0; i < this.size; ++i) {
            if (this.elements[i] != null && this.elements[i].equals(element)) {
                return true;
            }
        }

        return false;
    }

    public int indexOf(T element) {
        for(int i = 0; i < this.size; ++i) {
            if (this.elements[i] == null && element == null) {
                return i;
            }

            if (this.elements[i] != null && this.elements[i].equals(element)) {
                return i;
            }
        }

        return -1;
    }

    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int currentIndex;

            {
                Objects.requireNonNull(CustomList.this);
                this.currentIndex = 0;
            }

            public boolean hasNext() {
                return this.currentIndex < CustomList.this.size;
            }

            public T next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException("No more elements in CustomList");
                } else {
                    return (T)CustomList.this.elements[this.currentIndex++];
                }
            }
        };
    }

    public String toString() {
        if (this.size == 0) {
            return "[]";
        } else {
            StringBuilder sb = new StringBuilder("[");

            for(int i = 0; i < this.size; ++i) {
                sb.append(this.elements[i]);
                if (i < this.size - 1) {
                    sb.append(", ");
                }
            }

            sb.append("]");
            return sb.toString();
        }
    }

    public Stream<T> stream() {
        return StreamSupport.stream(Spliterators.spliterator(this.iterator(), (long)this.size, 0), false);
    }

    public static <T> CustomList<T> fromStream(Stream<T> stream) {
        CustomList<T> list = new CustomList<T>();
        stream.forEach((element) -> list.add(element));
        return list;
    }
}