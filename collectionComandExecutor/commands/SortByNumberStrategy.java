package collectionComandExecutor.commands;

import collectionComandExecutor.CollectionStrategy;
import data.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/** Стратегия сортировки по числовому полю (number). */
public class SortByNumberStrategy implements CollectionStrategy {

    @Override
    public void execute(List<Data> input) {
        if (input == null || input.size() < 2) return;

        Comparator<Data> cmp = Comparator.comparingInt(Data::getNumber);

        int mid = input.size() / 2;
        List<Data> left  = new ArrayList<>(input.subList(0, mid));
        List<Data> right = new ArrayList<>(input.subList(mid, input.size()));

        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            Future<?> f1 = pool.submit(() -> Data.bubbleSort(left,  cmp));
            Future<?> f2 = pool.submit(() -> Data.bubbleSort(right, cmp));
            f1.get();
            f2.get();

            List<Data> merged = merge(left, right, cmp);
            input.clear();
            input.addAll(merged);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            System.out.println("Сортировка прервана: " + ie.getMessage());
        } catch (ExecutionException ee) {
            System.out.println("Ошибка многопоточной сортировки: " + ee.getCause());
        } finally {
            pool.shutdown();
        }
    }

    /** Стабильное слияние двух уже отсортированных списков. */
    private static List<Data> merge(List<Data> left, List<Data> right, Comparator<Data> cmp) {
        List<Data> out = new ArrayList<>(left.size() + right.size());
        int i = 0, j = 0;
        while (i < left.size() && j < right.size()) {
            if (cmp.compare(left.get(i), right.get(j)) <= 0) {
                out.add(left.get(i++));
            } else {
                out.add(right.get(j++));
            }
        }
        while (i < left.size()) out.add(left.get(i++));
        while (j < right.size()) out.add(right.get(j++));
        return out;
    }
}
