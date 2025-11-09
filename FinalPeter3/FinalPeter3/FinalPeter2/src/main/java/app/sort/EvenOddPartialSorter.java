package app.sort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public final class EvenOddPartialSorter {

    private EvenOddPartialSorter() {}

    public static <T> void sort(T[] data,
                                Function<T, Integer> keyExtractor,
                                Comparator<T> comparator,
                                SortStrategy<T> innerSorter) {
        if (data == null || data.length < 2) return;
        List<Integer> evenIndices = new ArrayList<>();
        List<T> evens = new ArrayList<>();

        for (int i = 0; i < data.length; i++) {
            int key = keyExtractor.apply(data[i]);
            if ((key & 1) == 0) {
                evenIndices.add(i);
                evens.add(data[i]);
            }
        }
        if (evens.size() < 2) return;

        @SuppressWarnings("unchecked")
        T[] evArr = (T[]) new Object[evens.size()];
        for (int i = 0; i < evens.size(); i++) {
            evArr[i] = evens.get(i);
        }

        innerSorter.sort(evArr, comparator);

        for (int i = 0; i < evenIndices.size(); i++) {
            data[evenIndices.get(i)] = evArr[i];
        }
    }
}
