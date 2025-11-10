package collectionComandExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class OccurrenceCounter {
    private OccurrenceCounter() {
    }

    public static <T> long countOccurrencesParallel(T[] data, T target, int threads) {
        if (data != null && data.length != 0) {
            int n = data.length;
            int threadCount = Math.max(2, Math.min(threads, n));
            int chunk = (n + threadCount - 1) / threadCount;
            ExecutorService exec = Executors.newFixedThreadPool(threadCount);

            long var26;
            try {
                List<Future<Long>> futures = new ArrayList();

                for(int i = 0; i < threadCount; ++i) {
                    int start = i * chunk;
                    int end = Math.min(start + chunk, n);
                    if (start >= end) {
                        break;
                    }

                    futures.add(exec.submit(() -> {
                        long cnt = 0L;

                        for(int k = start; k < end; ++k) {
                            if (Objects.equals(target, data[k])) {
                                ++cnt;
                            }
                        }

                        return cnt;
                    }));
                }

                long sum = 0L;

                for(Future<Long> f : futures) {
                    try {
                        sum += (Long)f.get();
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException(ie);
                    } catch (ExecutionException ee) {
                        throw new RuntimeException(ee.getCause());
                    }
                }

                var26 = sum;
            } finally {
                exec.shutdown();

                try {
                    if (!exec.awaitTermination(10L, TimeUnit.SECONDS)) {
                        exec.shutdownNow();
                    }
                } catch (InterruptedException var20) {
                    exec.shutdownNow();
                    Thread.currentThread().interrupt();
                }

            }

            return var26;
        } else {
            return 0L;
        }
    }
    /**
     * Новый многопоточный метод для подсчета вхождений с использованием предиката (условия).
     */
    public static <T> long countOccurrencesByPredicateParallel(List<T> dataList, Predicate<T> condition, int threads) {
        if (dataList == null || dataList.isEmpty()) {
            return 0L;
        }

        int n = dataList.size();
        // Ограничиваем количество потоков
        int threadCount = Math.max(1, Math.min(threads, Runtime.getRuntime().availableProcessors()));
        int chunk = (n + threadCount - 1) / threadCount;
        ExecutorService exec = Executors.newFixedThreadPool(threadCount);

        List<Future<Long>> futures = new ArrayList<>();

        try {
            for (int i = 0; i < threadCount; ++i) {
                final int start = i * chunk;
                final int end = Math.min(start + chunk, n);

                if (start >= end) break;

                // Запускаем задачу в отдельном потоке
                futures.add(exec.submit(() -> {
                    long cnt = 0L;
                    // Каждый поток обрабатывает свой диапазон списка
                    for (int k = start; k < end; ++k) {
                        if (condition.test(dataList.get(k))) { // Используем переданный предикат
                            cnt++;
                        }
                    }
                    return cnt;
                }));
            }

            long sum = 0L;
            for (Future<Long> f : futures) {
                sum += f.get(); // Объединяем результаты
            }
            return sum;

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Ошибка выполнения многопоточного подсчета", e);
        } finally {
            exec.shutdown();
            try {
                if (!exec.awaitTermination(10L, TimeUnit.SECONDS)) {
                    exec.shutdownNow();
                }
            } catch (InterruptedException e) {
                exec.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}