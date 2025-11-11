import searchCommandExecutor.commands.BinarySearchStrategy;
import collectionComandExecutor.commands.CountStrategy;
import collectionComandExecutor.commands.SortByLetterStrategy;
import collectionComandExecutor.commands.SortBySpecialStrategy;
import data.Data;
import data.DataBuilder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TestStrategies {

    // Вспомогательный метод для создания базового массива данных
    private static Data[] baseArray() {
        return new Data[]{
                (new DataBuilder()).setLetter("C").setNumber(26).setLogical(true).build(),
                (new DataBuilder()).setLetter("A").setNumber(5).setLogical(false).build(),
                (new DataBuilder()).setLetter("B").setNumber(57).setLogical(true).build(),
                (new DataBuilder()).setLetter("E").setNumber(68).setLogical(false).build(),
                (new DataBuilder()).setLetter("D").setNumber(1).setLogical(true).build()
        };
    }

    // Вспомогательный метод для сравнения списков
    private static boolean listsEqual(List<Data> list1, List<Data> list2) {
        if (list1.size() != list2.size()) return false;
        for (int i = 0; i < list1.size(); i++) {
            if (!list1.get(i).toString().equals(list2.get(i).toString())) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) throws NoSuchFieldException {
        System.out.println("--- Запуск тестов ---");

        testSortByLetterStrategy();
        testSortBySpecialStrategy();
        testBinarySearchStrategy();
        testCountStrategy();
    }

    // --- Тест для SortByLetterStrategy ---
    public static void testSortByLetterStrategy() {
        System.out.println("\n--- Тест: SortByLetterStrategy ---");
        List<Data> input = new ArrayList<>(Arrays.asList(baseArray()));
        SortByLetterStrategy strategy = new SortByLetterStrategy();
        List<Data> expected = Arrays.asList(
                (new DataBuilder()).setLetter("A").setNumber(5).setLogical(false).build(),
                (new DataBuilder()).setLetter("B").setNumber(57).setLogical(true).build(),
                (new DataBuilder()).setLetter("C").setNumber(26).setLogical(true).build(),
                (new DataBuilder()).setLetter("D").setNumber(1).setLogical(true).build(),
                (new DataBuilder()).setLetter("E").setNumber(68).setLogical(false).build()
        );

        strategy.execute(input);

        if (listsEqual(input, expected)) {
            System.out.println("! Успех: Список отсортирован по полю 'letter' корректно.");
        } else {
            System.out.println("х Ошибка: Сортировка по полю 'letter' неверна.");
             System.out.println("Ожидалось: " + expected);
             System.out.println("Получено:   " + input);
        }
    }

    // --- Тест для SortBySpecialStrategy ---
    public static void testSortBySpecialStrategy() {
        System.out.println("\n--- Тест: SortBySpecialStrategy ---");
        List<Data> input = new ArrayList<>(Arrays.asList(baseArray()));
        SortBySpecialStrategy strategy = new SortBySpecialStrategy();

        List<Data> expected = Arrays.asList(
                (new DataBuilder()).setLetter("C").setNumber(26).setLogical(true).build(),
                (new DataBuilder()).setLetter("E").setNumber(68).setLogical(false).build(),
                (new DataBuilder()).setLetter("A").setNumber(5).setLogical(false).build(),
                (new DataBuilder()).setLetter("B").setNumber(57).setLogical(true).build(),
                (new DataBuilder()).setLetter("D").setNumber(1).setLogical(true).build()
        );

        strategy.execute(input);

        if (listsEqual(input, expected)) {
            System.out.println("! Успех: Специальная сортировка работает корректно.");
        } else {
            System.out.println("х Ошибка: Специальная сортировка неверна.");
            System.out.println("Ожидалось: " + expected);
            System.out.println("Получено:   " + input);
        }
    }

    // --- Тест для BinarySearchStrategy ---
    public static void testBinarySearchStrategy() throws NoSuchFieldException {
        System.out.println("\n--- Тест: BinarySearchStrategy ---");
        // Бинарный поиск требует отсортированного списка.
        List<Data> sortedInput = Arrays.asList(
                (new DataBuilder()).setLetter("E").setNumber(1).setLogical(true).build(),
                (new DataBuilder()).setLetter("A").setNumber(5).setLogical(false).build(),
                (new DataBuilder()).setLetter("C").setNumber(26).setLogical(true).build(),
                (new DataBuilder()).setLetter("B").setNumber(57).setLogical(true).build(),
                (new DataBuilder()).setLetter("D").setNumber(68).setLogical(false).build()
        );

        // Подмена System.in для симуляции ввода пользователя
        String simulatedInput = "26\n";
        InputStream originalIn = System.in;
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        Scanner scanner = new Scanner(System.in);

        Field numberField = Data.class.getDeclaredField("number");
        BinarySearchStrategy strategy = new BinarySearchStrategy(numberField, scanner);
        List<Data> expectedResult = List.of(sortedInput.get(2));

        List<Data> result = strategy.execute(sortedInput);

        if (listsEqual(result, expectedResult)) {
            System.out.println("! Успех: Бинарный поиск нашел верный элемент.");
        } else {
            System.out.println("х Ошибка: Бинарный поиск неверный.");
        }
        System.setIn(originalIn);
    }

    // --- Тест для CountStrategy ---
    public static void testCountStrategy() throws NoSuchFieldException {
        System.out.println("\n--- Тест: CountStrategy ---");
        List<Data> input = new ArrayList<>(Arrays.asList(baseArray()));

        // Подмена System.in для симуляции ввода пользователя
        String simulatedInput = "true\n";
        InputStream originalIn = System.in;
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        Scanner scanner = new Scanner(System.in);

        Field logicalField = Data.class.getDeclaredField("logical");
        CountStrategy strategy = new CountStrategy(logicalField, scanner);

        System.out.println("Ожидаем вывод количества вхождений 'true' (должно быть 3) в консоль:");

        try {
            strategy.execute(input);
            System.out.println("! Успех: Метод CountStrategy выполнен без ошибок.");
        } catch (Exception e) {
            System.out.println("х Ошибка: Метод CountStrategy вызвал исключение: " + e.getMessage());
        }
        System.setIn(originalIn);
    }
}