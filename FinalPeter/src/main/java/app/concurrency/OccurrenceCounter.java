package app.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public final class OccurrenceCounter {
    private OccurrenceCounter() {}

    public static <T> long countOccurrencesParallel(T[] data, T target, int threads) {
        if (data == null || data.length == 0) return 0;
        if (threads < 2) threads = 2;

        ExecutorService exec = Executors.newFixedThreadPool(threads);
        List<Future<Long>> futures = new ArrayList<>();

        int n = data.length;
        int chunk = Math.max(1, n / threads);
        int start = 0;
        for (int i = 0; i < threads; i++) {
            int end = (i == threads - 1) ? n : Math.min(n, start + chunk);
            final int s = start;
            final int e = end;
            Future<Long> f = exec.submit(() -> {
                long cnt = 0;
                for (int k = s; k < e; k++) {
                    if (target == null) {
                        if (data[k] == null) cnt++;
                    } else if (target.equals(data[k])) {
                        cnt++;
                    }
                }
                return cnt;
            });
            futures.add(f);
            start = end;
        }
        long total = 0;
        for (Future<Long> f : futures) {
            try {
                total += f.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        exec.shutdown();
        return total;
    }
}
