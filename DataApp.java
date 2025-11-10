

import collectionComandExecutor.CollectionCommandExecutor;
import collectionComandExecutor.commands.*;
import data.Data;
import inputCommandExecutor.InputCommandExecutor;
import inputCommandExecutor.commands.*;
import searchCommandExecutor.SearchCommandExecutor;
import searchCommandExecutor.commands.BinarySearchStrategy;
import searchCommandExecutor.commands.EmptySearchStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataApp {

    private static final Scanner scanner = new Scanner(System.in);
    private static List<Data> dataList = new ArrayList<>();
    private static CollectionCommandExecutor collectionCommandExecutor = new CollectionCommandExecutor(new EmptyCollectionStrategy());
    private static InputCommandExecutor inputCommandExecutor = new InputCommandExecutor(new EmptyInputStrategy());
    private static SearchCommandExecutor searchCommandExecutor = new SearchCommandExecutor(new EmptySearchStrategy());

    public static void main(String[] args) throws NoSuchFieldException {
        while (true) {
            printMainMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> {
                    inputDataMenu();
                }
                case 2 -> {
                    sortMenu();
                }
                case 3 -> {
                    binarySearchMenu();
                }
                case 4 -> {
                    collectionCommandExecutor.setStrategy(new TransferToCustomCollectionStrategy());
                    collectionCommandExecutor.executeCommand(dataList);
                }
                case 5 -> {
                    countStrategyMeny();
//                    collectionCommandExecutor.setStrategy(new CountStrategy());
//                    collectionCommandExecutor.executeCommand(dataList);
                }
                case 6 -> {
                    return;
                }
                default -> System.out.println("Нет такого пункта меню");
            }
        }
    }

    private static void printMainMenu() {
        System.out.println("1. Получить данные");
        System.out.println("2. Отсортировать");
        System.out.println("3. Бинарный поиск");
        System.out.println("4. Передать данные в кастомные коллекции (доп 3)");
        System.out.println("5. Посчитать количество вхождений элемента N в коллекцию (доп 4)");
        System.out.println("6. Выход");
        System.out.println("Выбор (1-6):");
    }

    private static void inputDataMenu() {
        System.out.println("1. Ввести в ручную");
        System.out.println("2. Заполнить случайно");
        System.out.println("3. Из файла");
        System.out.println("4. Назад");
        System.out.println("Выбор (1-4):");

        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1 -> {
                inputCommandExecutor.setStrategy(new InputManualStrategy(scanner));
                dataList = inputCommandExecutor.executeCommand();
            }
            case 2 -> {
                inputCommandExecutor.setStrategy(new InputRandomStrategy(scanner));
                dataList = inputCommandExecutor.executeCommand();
            }
            case 3 -> {
                inputCommandExecutor.setStrategy(new InputFromFileStrategy(scanner));
                dataList = inputCommandExecutor.executeCommand();
            }
            case 4 -> {
            }
            default -> System.out.println("Нет такого пункта меню");

        }
    }

    private static void sortMenu() {
        System.out.println("1. По строковому полю");
        System.out.println("2. По числовому полю");
        System.out.println("3. По логическому полю");
        System.out.println("4. Особая сортировка (доп 1)");
        System.out.println("5.Назад");
        System.out.println("Выбор (1-5):");

        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1 -> {
                collectionCommandExecutor.setStrategy(new SortByLetterStrategy());
                finalSortCommandsRuner();
            }
            case 2 -> {
                collectionCommandExecutor.setStrategy(new SortByNumberStrategy());
                finalSortCommandsRuner();
            }
            case 3 -> {
                collectionCommandExecutor.setStrategy(new SortByLogicalStrategy());
                finalSortCommandsRuner();
            }
            case 4 -> {
                collectionCommandExecutor.setStrategy(new SortBySpecialStrategy());
                finalSortCommandsRuner();
            }
            case 5 -> {
            }
            default -> System.out.println("Нет такого пункта меню");

        }
    }

    private static void binarySearchMenu() throws NoSuchFieldException {
        System.out.println("1. По строковому полю");
        System.out.println("2. По числовому полю");
        System.out.println("3. По логическому полю");
        System.out.println("4. Назад");
        System.out.println("Выбор (1-5):");

        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1 -> {
                searchCommandExecutor.setStrategy(new BinarySearchStrategy(
                        Data.class.getDeclaredField("letter"),
                        scanner
                ));
                finalSearchCommandsRuner();
            }
            case 2 -> {
                searchCommandExecutor.setStrategy(new BinarySearchStrategy(
                        Data.class.getDeclaredField("number"),
                        scanner
                ));
                finalSearchCommandsRuner();
            }
            case 3 -> {
                searchCommandExecutor.setStrategy(new BinarySearchStrategy(
                        Data.class.getDeclaredField("logical"),
                        scanner
                ));
                finalSearchCommandsRuner();
            }
            case 4 -> {
            }
            default -> System.out.println("Нет такого пункта меню");
        }
    }

    private static void countStrategyMeny() throws NoSuchFieldException {
        System.out.println("1. По строковому полю");
        System.out.println("2. По числовому полю");
        System.out.println("3. По логическому полю");
        System.out.println("4. Назад");
        System.out.println("Выбор (1-4):");

        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1 -> {
                collectionCommandExecutor.setStrategy(new CountStrategy(
                        Data.class.getDeclaredField("letter"),
                        scanner
                ));
                finalCountCommandsRuner();
            }
            case 2 -> {
                collectionCommandExecutor.setStrategy(new CountStrategy(
                        Data.class.getDeclaredField("number"),
                        scanner
                ));
                finalCountCommandsRuner();
            }
            case 3 -> {
                collectionCommandExecutor.setStrategy(new CountStrategy(
                        Data.class.getDeclaredField("logical"),
                        scanner
                ));
                finalCountCommandsRuner();
            }
            case 4 -> {
            }
            default -> System.out.println("Нет такого пункта меню");
        }
    }

    private static void finalSortCommandsRuner() {
        collectionCommandExecutor.executeCommand(dataList);
        printSortResult();
        collectionCommandExecutor.setStrategy(new SaveIntoFileStrategy(scanner));
        collectionCommandExecutor.executeCommand(dataList);
    }

    private static void printSortResult() {
        System.out.println("Отсортированные данные:");
        for (Data data : dataList) {
            System.out.println(data);
        }
    }

    private static void finalSearchCommandsRuner() {
        List<Data> result = searchCommandExecutor.executeCommand(dataList);
        printSearchResult(result);
        collectionCommandExecutor.setStrategy(new SaveIntoFileStrategy(scanner));
        collectionCommandExecutor.executeCommand(result);
    }

    private static void printSearchResult(List<Data> result) {
        if (result.isEmpty()) {
            System.out.println("Ничего не найдено");
        } else {
            System.out.println("Найденные данные:");
            for (Data data : result) {
                System.out.println(data);
            }
        }
    }

    private static void finalCountCommandsRuner() {
        collectionCommandExecutor.executeCommand(dataList);
        collectionCommandExecutor.setStrategy(new SaveIntoFileStrategy(scanner));
        collectionCommandExecutor.executeCommand(dataList);
    }
}
