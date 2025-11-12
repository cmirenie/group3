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

/**
 * Многопоточная сортировка по булевому полю Data::isLogical.
 * Делим коллекцию на 2 части, сортируем обе пузырьком в пуле из 2 потоков,
 * затем стабильно сливаем.
 */
public class SortByLogicalStrategy implements CollectionStrategy {

    @Override
    public void execute(List<Data> input) {
        if (input == null || input.isEmpty()) {
            return;
        }

        Comparator<Data> cmp = Comparator.comparing(Data::isLogical);

        int mid = input.size() / 2;
        List<Data> left = new ArrayList<>(input.subList(0, mid));
        List<Data> right = new ArrayList<>(input.subList(mid, input.size()));

        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            Future<?> f1 = pool.submit(() -> Data.bubbleSort(left, cmp));
            Future<?> f2 = pool.submit(() -> Data.bubbleSort(right, cmp));

            f1.get();
            f2.get();

            List<Data> merged = merge(left, right, cmp);
            input.clear();
            input.addAll(merged);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Сортировка прервана: " + e.getMessage());
        } catch (ExecutionException e) {
            System.out.println("Ошибка многопоточной сортировки: " + e.getCause());
        } finally {
            pool.shutdown();
        }
    }

    /** Стабильное слияние двух уже отсортированных списков. */
    private static List<Data> merge(List<Data> left, List<Data> right, Comparator<Data> cmp) {
        List<Data> out = new ArrayList<>(left.size() + right.size());
        int i = 0;
        int j = 0;

        while (i < left.size() && j < right.size()) {
            Data li = left.get(i);
            Data rj = right.get(j);
            if (cmp.compare(li, rj) <= 0) { // <= — стабильно
                out.add(li);
                i++;
            } else {
                out.add(rj);
                j++;
            }
        }
        while (i < left.size()) out.add(left.get(i++));
        while (j < right.size()) out.add(right.get(j++));
        return out;
    }
}
