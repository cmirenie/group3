package app.collection;

import java.util.*;
import java.util.function.Consumer;

public class MyArrayList<T> implements Iterable<T> {
    private Object[] data;
    private int size;

    public MyArrayList() {
        this.data = new Object[10];
        this.size = 0;
    }

    public static <E> MyArrayList<E> fromArray(E[] arr) {
        MyArrayList<E> list = new MyArrayList<>();
        for (E e : arr) list.add(e);
        return list;
    }

    public void add(T value) {
        ensureCapacity(size + 1);
        data[size++] = value;
    }

    public T get(int index) {
        rangeCheck(index);
        @SuppressWarnings("unchecked")
        T v = (T) data[index];
        return v;
    }

    public T set(int index, T value) {
        rangeCheck(index);
        @SuppressWarnings("unchecked")
        T old = (T) data[index];
        data[index] = value;
        return old;
    }

    public T remove(int index) {
        rangeCheck(index);
        @SuppressWarnings("unchecked")
        T old = (T) data[index];
        int move = size - index - 1;
        if (move > 0) {
            System.arraycopy(data, index + 1, data, index, move);
        }
        data[--size] = null;
        return old;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public T[] toArray(T[] a) {
        int n = size;
        Class<?> comp = a.getClass().getComponentType();

        if (a.length < n) {
            @SuppressWarnings("unchecked")
            T[] r = (T[]) java.lang.reflect.Array.newInstance(comp, n);
            for (int i = 0; i < n; i++) {
                @SuppressWarnings("unchecked")
                T e = (T) data[i];
                r[i] = e;
            }
            return r;
        } else {
            for (int i = 0; i < n; i++) {
                @SuppressWarnings("unchecked")
                T e = (T) data[i];
                a[i] = e;
            }
            if (a.length > n) a[n] = null;
            return a;
        }
    }

    public Object[] toArray() {
        Object[] r = new Object[size];
        System.arraycopy(this.data, 0, r, 0, size);
        return r;
    }

    private void ensureCapacity(int minCap) {
        if (minCap <= data.length) return;
        int newCap = Math.max(minCap, data.length + (data.length >> 1) + 1);
        data = Arrays.copyOf(data, newCap);
    }

    private void rangeCheck(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index=" + index + ", size=" + size);
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            int cursor = 0;
            @Override public boolean hasNext() { return cursor < size; }
            @Override public T next() {
                if (cursor >= size) throw new NoSuchElementException();
                @SuppressWarnings("unchecked") T v = (T) data[cursor++];
                return v;
            }
        };
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        for (int i = 0; i < size; i++) {
            @SuppressWarnings("unchecked") T v = (T) data[i];
            action.accept(v);
        }
    }
}
