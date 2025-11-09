package app.sort;

import app.util.ExecutorHolder;

import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ParallelMergeSortStrategy<T> implements SortStrategy<T> {
    @Override
    public void sort(T[] data, Comparator<T> comparator) {
        if (data == null || data.length < 2) return;
        ExecutorService exec = ExecutorHolder.get();
        @SuppressWarnings("unchecked")
        T[] aux = (T[]) new Object[data.length];
        mergeSort(exec, data, aux, 0, data.length - 1, comparator, 0);
    }

    private void mergeSort(ExecutorService exec, T[] a, T[] aux, int lo, int hi, Comparator<T> cmp, int depth) {
        if (lo >= hi) return;
        int mid = (lo + hi) >>> 1;

        if (depth < Runtime.getRuntime().availableProcessors()) {
            Future<?> left = exec.submit(() -> mergeSort(exec, a, aux, lo, mid, cmp, depth + 1));
            mergeSort(exec, a, aux, mid + 1, hi, cmp, depth + 1);
            try {
                left.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            mergeSort(exec, a, aux, lo, mid, cmp, depth + 1);
            mergeSort(exec, a, aux, mid + 1, hi, cmp, depth + 1);
        }
        merge(a, aux, lo, mid, hi, cmp);
    }

    private void merge(T[] a, T[] aux, int lo, int mid, int hi, Comparator<T> cmp) {
        System.arraycopy(a, lo, aux, lo, hi - lo + 1);
        int i = lo;
        int j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) {
                a[k] = aux[j++];
            } else if (j > hi) {
                a[k] = aux[i++];
            } else if (cmp.compare(aux[i], aux[j]) <= 0) {
                a[k] = aux[i++];
            } else {
                a[k] = aux[j++];
            }
        }
    }
}
