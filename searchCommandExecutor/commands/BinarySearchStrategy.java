package searchCommandExecutor.commands;

import data.Data;
import searchCommandExecutor.SearchStrategy;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Стратегия бинарного поиска по всем полям
 * Целевое поле параметризировано
 */
public class BinarySearchStrategy implements SearchStrategy {
    private Field field;
    private Scanner scanner;

    public BinarySearchStrategy(Field field, Scanner scanner) {
        this.field = field;
        this.scanner = scanner;
    }

    @Override
    public List<Data> execute(List<Data> input) {
        mainLoop:
        while (true) {
            Object valueForSearch = getUserInput();
            if (valueForSearch == null) {
                while (true) {
                    System.out.println("Попробовать еще раз? (Д/Н)");
                    String choice = scanner.nextLine();
                    if (choice.compareTo("Н") == 0) {
                        return List.of();
                    } else if (choice.compareTo("Д") == 0) {
                        continue mainLoop;
                    } else {
                        System.out.println("Ошибка: данного варианта выбора не существует.");
                    }
                }
            }
            return binarySearch(input, valueForSearch);
        }
    }

    // Метод получения значения через консоль от пользователя
    public <T> T getUserInput() {
        Class<?> fieldType = field.getType();
        System.out.println("Введите значение для поиска по полю:");
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

    // Метод для бинарного поиска
    private <T> List<Data> binarySearch(List<Data> dataList, T value) {
        if (!isSorted(dataList)) {
            System.out.println("Список не отсортирован! Бинарный поиск невозможен.");
            return Collections.emptyList();
        }
        int lowIndex = 0;
        int highIndex = dataList.size() - 1;
        List<Data> result = new ArrayList<>();
        while (lowIndex <= highIndex) {
            int middleIndex = lowIndex + (highIndex - lowIndex) / 2;
            Data midData = dataList.get(middleIndex);
            try {
                Comparable fieldValue = (Comparable) field.get(midData);
                int cmp = fieldValue.compareTo(value);
                if (cmp == 0) {
                    result.add(midData);
                    return result;
                } else if (cmp < 0) {
                    lowIndex = middleIndex + 1;
                } else {
                    highIndex = middleIndex - 1;
                }
            } catch (IllegalAccessException e) {
                System.out.println("Ошибка доступа к полю объекта: " + e.getMessage());
                return Collections.emptyList();
            }
        }
        return result;
    }

    // Метод проверки, отсортирован ли список
    private boolean isSorted(List<Data> dataList) {
        field.setAccessible(true);
        for (int i = 1; i < dataList.size(); i++) {
            try {
                Comparable val1 = (Comparable) field.get(dataList.get(i - 1));
                Comparable val2 = (Comparable) field.get(dataList.get(i));
                if (val1.compareTo(val2) > 0) {
                    return false;
                }
            } catch (IllegalAccessException e) {
                System.out.println("Ошибка доступа к полям при проверке сортировки: " + e.getMessage());
                return false;
            }
        }
        return true;
    }
}