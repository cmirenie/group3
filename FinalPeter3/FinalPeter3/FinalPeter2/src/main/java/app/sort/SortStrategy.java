package app.sort;

import java.util.Comparator;

public interface SortStrategy<T> {
    void sort(T[] data, Comparator<T> comparator);
}
