

import collectionComandExecutor.CollectionCommandExecutor;
import collectionComandExecutor.commands.*;
import data.Data;
import inputCommandExecutor.InputCommandExecutor;
import inputCommandExecutor.commands.*;
import searchCommandExecutor.SearchCommandExecutor;
import searchCommandExecutor.commands.EmptySearchStrategy;
import searchCommandExecutor.commands.SearchByLetterStrategy;
import searchCommandExecutor.commands.SearchByLogicalStrategy;
import searchCommandExecutor.commands.SearchByNumberStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataApp {

    private static final Scanner scanner = new Scanner(System.in);
    private static List<Data> dataList = new ArrayList<>();
    public static void main(String[] args) {
        while (true) {
            printMainMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> {
                    inputDataMenu();
                }
                break;

            default:
                System.out.println("Неверный выбор.");
                break;
        }
        dataList.sort(new Comparator<Data>() {
            @Override
            public int compare(Data d1, Data d2) {
                boolean d1Even = d1.getNumber() % 2 == 0;
                boolean d2Even = d2.getNumber() % 2 == 0;

                if (d1Even && d2Even) {
                    return Integer.compare(d1.getNumber(), d2.getNumber());
                } else if (!d1Even && !d2Even) {
                    return 0;
                } else if (d1Even) {
                    return -1;
                } else {
                    return 1;
                case 2 -> {
                    sortMenu();
                }
                case 3 -> {
                    binarySearchMenu();
                }
                case 4 -> {
                }
                case 5 -> {
                }
                case 6 -> {
                    return;
                }
                default -> System.out.println("Нет такого пункта меню");
            }
        });
        sortEvenNumbers(dataList);
        System.out.println("Сохраненные данные:");
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
            }
            case 2 -> {
            }
            case 3 -> {
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
            }
            case 2 -> {
            }
            case 3 -> {
            }
            case 4 -> {
            }
            case 5 -> {
            }
            default -> System.out.println("Нет такого пункта меню");

        }
    }

    private static void binarySearchMenu() {
        System.out.println("1. По строковому полю");
        System.out.println("2. По числовому полю");
        System.out.println("3. По логическому полю");
        System.out.println("4. Назад");
        System.out.println("Выбор (1-5):");

        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1 -> {
            }
            case 2 -> {
            }
            case 3 -> {
            }
            case 4 -> {
            }
            default -> System.out.println("Нет такого пункта меню");
        }
    }

        for (Data data : dataList) {
            System.out.println(data);
        }
    }

    public static void sortEvenNumbers(List<Data> list) {
        List<Integer> evenIndices = new ArrayList<>();
        List<Data> evenValues = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getNumber() % 2 == 0) {
                evenIndices.add(i);
                evenValues.add(list.get(i));
            }
        }

        evenValues.sort(Comparator.comparingInt(Data::getNumber));

        for (int i = 0; i < evenIndices.size(); i++) {
            list.set(evenIndices.get(i), evenValues.get(i));
        }
    }
}
