package data;

import java.util.Comparator;
import java.util.List;

public record Data(String letter, int number, boolean logical)
        implements Comparable<Data> {

    // Совместимость со старым кодом
    public String getLetter() { return letter; }
    public int getNumber() { return number; }
    public boolean isLogical() { return logical; }

    @Override
    public int compareTo(Data o) {
        if (o == null) return 1;
        int byLetter = String.valueOf(letter).compareTo(String.valueOf(o.letter));
        if (byLetter != 0) return byLetter;
        int byNumber = Integer.compare(number, o.number);
        if (byNumber != 0) return byNumber;
        return Boolean.compare(logical, o.logical);
        // equals/hashCode/toString сгенерирует record
    }

    /** Пузырьковая сортировка с компаратором. */
    public static <T> void bubbleSort(List<T> list, Comparator<? super T> cmp) {
        if (list == null || list.size() < 2) return;
        int n = list.size();
        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                if (cmp.compare(list.get(j), list.get(j + 1)) > 0) {
                    T tmp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, tmp);
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }
}
