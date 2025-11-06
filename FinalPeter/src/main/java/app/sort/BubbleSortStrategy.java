package app.sort;

import java.util.Comparator;

public class BubbleSortStrategy<T> implements SortStrategy<T> {
    @Override
    public void sort(T[] data, Comparator<T> comparator) {
        if (data == null || data.length < 2) return;
        for (int i = 0; i < data.length - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < data.length - i - 1; j++) {
                T a = data[j];
                T b = data[j + 1];
                if (comparator.compare(a, b) > 0) {
                    data[j] = b;
                    data[j + 1] = a;
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }
}
