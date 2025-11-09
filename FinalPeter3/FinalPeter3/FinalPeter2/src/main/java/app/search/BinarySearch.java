package app.search;

import java.util.Comparator;

public final class BinarySearch {
    private BinarySearch() {}

    public static <T> int binarySearch(T[] array, T key, Comparator<T> comparator) {
        int low = 0;
        int high = array.length - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            T midVal = array[mid];
            int cmp = comparator.compare(key, midVal);
            if (cmp < 0) {
                high = mid - 1;
            } else if (cmp > 0) {
                low = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }
}
