package app.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public final class OccurrenceCounter {
    private OccurrenceCounter() { }

    public static <T> long countOccurrencesParallel(T[] data, T target, int threads) {
        if (data == null || data.length == 0) return 0L;

        int n = data.length;
        int threadCount = Math.max(2, Math.min(threads, n));
        int chunk = (n + threadCount - 1) / threadCount;

        ExecutorService exec = Executors.newFixedThreadPool(threadCount);
        try {
            List<Future<Long>> futures = new ArrayList<>();

            for (int i = 0; i < threadCount; i++) {
                final int start = i * chunk;
                final int end = Math.min(start + chunk, n);
                if (start >= end) break;

                futures.add(exec.submit(() -> {
                    long cnt = 0L;
                    for (int k = start; k < end; k++) {
                        if (Objects.equals(target, data[k])) {
                            cnt++;
                        }
                    }
                    return cnt;
                }));
            }

            long sum = 0L;
            for (Future<Long> f : futures) {
                try {
                    sum += f.get();
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(ie);
                } catch (ExecutionException ee) {
                    throw new RuntimeException(ee.getCause());
                }
            }
            return sum;
        } finally {
            exec.shutdown();
            try {
                if (!exec.awaitTermination(10, TimeUnit.SECONDS)) {
                    exec.shutdownNow();
                }
            } catch (InterruptedException ie) {
                exec.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}
