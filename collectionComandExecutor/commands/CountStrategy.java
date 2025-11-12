// file: collectionComandExecutor/commands/CountStrategy.java
package collectionComandExecutor.commands;

import collectionComandExecutor.CollectionStrategy;
import collectionComandExecutor.OccurrenceCounter;
import data.Data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * Задание 4: многопоточный подсчёт количества вхождений
 * выбранного значения по переданному полю объекта Data.
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
            System.out.println("Значение для подсчёта не задано.");
            return;
        }

        // Проецируем поле в отдельный список значений
        List<Object> column = new ArrayList<>(input.size());
        for (Data d : input) {
            try {
                column.add(field.get(d));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        int threads = Math.max(2, Runtime.getRuntime().availableProcessors() / 2);
        long count = OccurrenceCounter.countOccurrencesParallel(column, target, threads);

        System.out.printf(
                "Количество вхождений значения '%s' по полю '%s': %d%n",
                stringify(target), field.getName(), count
        );
    }

    /**
     * Чтение значения-цели с учётом типа поля.
     */
    private Object readTargetValue() {
        Class<?> type = field.getType();
        System.out.printf("Введите значение для поля '%s' (%s): ",
                field.getName(), type.getSimpleName());
        String raw = scanner.nextLine().trim();

        try {
            if (type == String.class) return raw;
            if (type == int.class || type == Integer.class) return Integer.parseInt(raw);
            if (type == boolean.class || type == Boolean.class) {
                String v = raw.toLowerCase();
                if (Objects.equals(v, "1") || Objects.equals(v, "true") || Objects.equals(v, "да")) return true;
                if (Objects.equals(v, "0") || Objects.equals(v, "false") || Objects.equals(v, "нет")) return false;
                // если пользователь ввёл что-то иное — пробуем Boolean.parseBoolean
                return Boolean.parseBoolean(raw);
            }
            // если появятся иные типы — возвращаем как строку
            return raw;
        } catch (Exception ex) {
            System.out.println("Неверный формат ввода: " + ex.getMessage());
            return null;
        }
    }

    private static String stringify(Object o) {
        return (o instanceof String) ? (String) o : String.valueOf(o);
    }
}
