// file: collectionComandExecutor/OccurrenceCounter.java
package collectionComandExecutor;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.Predicate;

/**
 * Параллельный подсчёт количества вхождений.
 * Универсально: сравнение через Objects.equals() или по предикату.
 */
public final class OccurrenceCounter {
    private OccurrenceCounter() { }

    /** Удобная обёртка для массивов. */
    @SuppressWarnings("unused")
    public static <T> long countOccurrencesParallel(T[] data, T target, int threads) {
        if (data == null || data.length == 0) return 0L;
        return countOccurrencesByPredicateParallel(Arrays.asList(data), t -> Objects.equals(t, target), threads);
    }

    /** Подсчёт по значению с использованием Objects.equals(). */
    public static <T> long countOccurrencesParallel(List<T> data, T target, int threads) {
        return countOccurrencesByPredicateParallel(data, t -> Objects.equals(t, target), threads);
    }

    /** Подсчёт по предикату (для сложных правил сравнения). */
    public static <T> long countOccurrencesByPredicateParallel(
            List<T> data, Predicate<T> predicate, int threads
    ) {
        if (data == null || data.isEmpty()) return 0L;

        final int n = data.size();
        final int threadCount = Math.max(1, Math.min(threads, n));
        final int chunk = Math.max(1, (n + threadCount - 1) / threadCount);

        ExecutorService pool = Executors.newFixedThreadPool(threadCount);
        try {
            long total = 0L;
            int start = 0;
            while (start < n) {
                int end = Math.min(n, start + chunk);
                final int s = start;
                final int e = end;

                Future<Long> fut = pool.submit(() -> {
                    long cnt = 0L;
                    for (int i = s; i < e; i++) {
                        if (predicate.test(data.get(i))) cnt++;
                    }
                    return cnt;
                });

                total += fut.get();
                start = end;
            }
            return total;
        } catch (InterruptedException | ExecutionException ex) {
            Thread.currentThread().interrupt();
            System.out.println("Ошибка многопоточного подсчёта: " + ex.getMessage());
            return 0L;
        } finally {
            pool.shutdown();
        }
    }
}
