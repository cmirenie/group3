package collectionComandExecutor;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Простой динамический список без готовых реализаций List.
 * Реализует Iterable и базовые операции.
 */
@SuppressWarnings("unused") // класс и часть API могут не вызываться прямо сейчас
public class CustomList<T> implements Iterable<T> {
    private static final int DEFAULT_CAPACITY = 10;

    private Object[] elements;
    private int size;

    public CustomList() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    public CustomList(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Capacity < 0");
        }
        this.elements = new Object[Math.max(initialCapacity, DEFAULT_CAPACITY)];
        this.size = 0;
    }

    public CustomList(Iterable<? extends T> source) {
        this();
        addAll(source);
    }

    // ===== базовые операции =====

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        Arrays.fill(elements, 0, size, null);
        size = 0;
    }

    public void add(T value) {
        ensureCapacity(size + 1);
        elements[size++] = value;
    }

    public void addAll(Iterable<? extends T> src) {
        for (T v : src) {
            add(v);
        }
    }

    public T get(int index) {
        checkIndex(index);
        @SuppressWarnings("unchecked")
        T value = (T) elements[index];
        return value;
    }

    public void set(int index, T value) {
        checkIndex(index);
        elements[index] = value;
    }

    public T remove(int index) {
        checkIndex(index);
        @SuppressWarnings("unchecked")
        T old = (T) elements[index];
        int moved = size - index - 1;
        if (moved > 0) {
            System.arraycopy(elements, index + 1, elements, index, moved);
        }
        elements[--size] = null;
        return old;
    }

    public int indexOf(Object needle) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(elements[i], needle)) {
                return i;
            }
        }
        return -1;
    }

    // ===== массив/потоки/итераторы =====

    @SuppressWarnings("unused")
    public Object[] toArray() {
        return Arrays.copyOf(elements, size);
    }

    @SuppressWarnings("unused")
    public <A> A[] toArray(A[] a) {
        if (a.length < size) {
            @SuppressWarnings("unchecked")
            A[] resized = (A[]) Arrays.copyOf(elements, size, a.getClass());
            return resized;
        }
        for (int i = 0; i < size; i++) {
            @SuppressWarnings("unchecked")
            A v = (A) elements[i];
            a[i] = v;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @SuppressWarnings("unused")
    public Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private int cursor;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            public T next() {
                if (cursor >= size) {
                    throw new NoSuchElementException();
                }
                return get(cursor++);
            }
        };
    }

    @Override
    public Spliterator<T> spliterator() {
        return Spliterators.spliterator(
                iterator(),
                size,
                Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED
        );
    }

    // ===== служебные =====

    private void ensureCapacity(int minCapacity) {
        if (minCapacity <= elements.length) {
            return;
        }
        int newCap = Math.max(elements.length + (elements.length >> 1), minCapacity); // *1.5
        elements = Arrays.copyOf(elements, newCap);
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index=" + index + ", size=" + size);
        }
    }
    // 1) Фабрика из Stream
    public static <T> CustomList<T> fromStream(java.util.stream.Stream<? extends T> stream) {
        CustomList<T> list = new CustomList<>();
        stream.forEach(list::add);
        return list;
    }

    // 2) Проверка наличия элемента
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }
}
