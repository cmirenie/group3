package collectionComandExecutor.commands;

import collectionComandExecutor.CollectionStrategy;
import data.Data;
import collectionComandExecutor.OccurrenceCounter;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Predicate;

/**
 * Дополнительное задание 4: реализовать многопоточный метод,
 * подсчитывающий количество вхождений элемента N в коллекцию и выводящий результат в консоль.
 */

public class CountStrategy implements CollectionStrategy {
    private Field field;
    private Scanner scanner;

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

        Object valueForSearch = getUserInput();
        if (valueForSearch == null) {
            System.out.println("Не удалось получить корректное значение для поиска.");
            return;
        }

        // --- Многопоточный подсчет по полю ---

        // Создаем предикат (условие):
        Predicate<Data> searchCondition = dataItem -> {
            try {
                Object fieldValue = field.get(dataItem);
                return Objects.equals(fieldValue, valueForSearch);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Ошибка доступа к полю при проверке: " + field.getName(), e);
            }
        };

        // Используем новый многопоточный метод
        long count = OccurrenceCounter.countOccurrencesByPredicateParallel(
                input,
                searchCondition,
                4 // Например, используем 4 потока
        );

        System.out.println("--- Результат многопоточного подсчета ---");
        System.out.println("Поле поиска: " + field.getName());
        System.out.println("Искомое значение: " + valueForSearch);
        System.out.println("Количество вхождений: " + count);
    }

    // Метод получения значения через консоль от пользователя (скопирован из BinarySearchStrategy)
    public <T> T getUserInput() {
        Class<?> fieldType = field.getType();
        System.out.println("Введите значение для поиска по полю '" + field.getName() + "':");
        String input = scanner.nextLine();
        try {
            if (fieldType.equals(String.class)) {
                return (T) input;
            } else if (fieldType.equals(int.class) || fieldType.equals(Integer.class)) {
                return (T) Integer.valueOf(input);
            } else if (fieldType.equals(boolean.class) || fieldType.equals(Boolean.class)) {
                return (T) Boolean.valueOf(input);
            }
        } catch (Exception e) {
            System.out.println("Ошибка при преобразовании ввода: " + e.getMessage());
        }
        return null;
    }
}