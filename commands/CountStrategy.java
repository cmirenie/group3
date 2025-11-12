package collectionComandExecutor.commands;

import collectionComandExecutor.CollectionStrategy;
import data.Data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Доп.задание 4: многопоточный подсчёт количества вхождений по выбранному полю.
 */
public record CountStrategy(Field field, Scanner scanner) implements CollectionStrategy {

    public CountStrategy(Field field, Scanner scanner) {
        this.field = field;
        this.scanner = scanner;
        this.field.setAccessible(true);
    }

    @Override
    public void execute(List<Data> input) {
        if (input == null || input.isEmpty()) {
            System.out.println("Входной список пуст.");
            return;
        }

        Object target = readTargetValue();
        if (target == null) {
            System.out.println("Значение не задано — подсчёт отменён.");
            return;
        }

        int threads = Math.max(2, Math.min(Runtime.getRuntime().availableProcessors(), input.size()));
        ExecutorService pool = Executors.newFixedThreadPool(threads);

        try {
            List<Future<Integer>> futures = new ArrayList<>();
            int chunk = Math.max(1, input.size() / threads);
            int start = 0;

            while (start < input.size()) {
                int end = Math.min(input.size(), start + chunk);
                final int s = start; // делаем «effectively final» для лямбды
                futures.add(pool.submit(counterTask(input, s, end, target)));
                start = end;
            }

            int total = 0;
            for (Future<Integer> f : futures) {
                total += f.get();
            }

            System.out.printf(
                    "Количество вхождений значения '%s' по полю '%s': %d%n",
                    stringify(target), field.getName(), total
            );
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            System.out.println("Подсчёт прерван: " + ie.getMessage());
        } catch (ExecutionException ee) {
            System.out.println("Ошибка при подсчёте: " + ee.getCause());
        } finally {
            pool.shutdown();
        }
    }

    /**
     * Задача для подсчёта на отрезке [from; to).
     */
    private Callable<Integer> counterTask(List<Data> list, int from, int to, Object target) {
        return () -> {
            int cnt = 0;
            for (int i = from; i < to; i++) {
                Object value = field.get(list.get(i)); // авто boxing для примитивов
                if (Objects.equals(value, target)) cnt++;
            }
            return cnt;
        };
    }

    /**
     * Считываем искомое значение под тип выбранного поля.
     */
    private Object readTargetValue() {
        Class<?> type = field.getType();

        if (type == String.class) {
            System.out.print("Введите искомое значение (String): ");
            return scanner.nextLine();
        }
        if (type == int.class || type == Integer.class) {
            while (true) {
                System.out.print("Введите искомое значение (целое число): ");
                String s = scanner.nextLine().trim();
                try {
                    return Integer.parseInt(s);
                } catch (NumberFormatException ignore) {
                    System.out.println("Некорректное число, попробуйте ещё раз.");
                }
            }
        }
        if (type == boolean.class || type == Boolean.class) {
            while (true) {
                System.out.print("Введите искомое значение (true/false): ");
                String s = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
                if ("true".equals(s) || "false".equals(s)) return Boolean.parseBoolean(s);
                System.out.println("Введите строго true или false.");
            }
        }

        System.out.println("Неподдерживаемый тип поля: " + type.getSimpleName());
        return null;
    }

    private static String stringify(Object o) {
        return (o instanceof String str) ? str : String.valueOf(o);
    }
}
