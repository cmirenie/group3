package app;

import app.collection.MyArrayList;
import app.concurrency.OccurrenceCounter;
import app.factory.DataFactory;
import app.model.Person;
import app.search.BinarySearch;
import app.sort.BubbleSortStrategy;
import app.sort.EvenOddPartialSorter;
import app.sort.ParallelMergeSortStrategy;
import app.sort.SortStrategy;
import app.util.FileIO;
import app.util.InputUtil;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.Scanner;
import java.util.function.Function;

public class Main {

    private static final Scanner SC = new Scanner(System.in);
    private static final String OUTPUT_FILE = "output/results.txt";

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        MyArrayList<Person> list = new MyArrayList<>();
        SortStrategy<Person> bubble = new BubbleSortStrategy<>();
        SortStrategy<Person> parallelMerge = new ParallelMergeSortStrategy<>();

        while (true) {
            printMenu();
            int choice = InputUtil.readInt(SC, "Выберите пункт: ", 0, 12);

            if (choice == 0) {
                System.out.println("Выход. До встречи!");
                break;
            }

            switch (choice) {
                case 1 -> {
                    int n = InputUtil.readInt(SC, "Длина массива: ", 1, 2000000);
                    list = DataFactory.manualPersons(SC, n);
                    System.out.println("Создано элементов: " + list.size());
                }
                case 2 -> {
                    int n = InputUtil.readInt(SC, "Длина массива: ", 1, 2000000);
                    list = DataFactory.randomPersons(n);
                    System.out.println("Создано случайных элементов: " + list.size());
                }
                case 3 -> {
                    System.out.print("Путь к файлу (CSV name;age;id): ");
                    String path = SC.next();
                    list = FileIO.readPersonsFromCsv(new File(path));
                    System.out.println("Загружено элементов: " + list.size());
                }
                case 4 -> {
                    if (list.isEmpty()) {
                        System.out.println("Сначала заполните коллекцию.");
                        break;
                    }
                    Person[] arr = list.toArray(new Person[0]);
                    System.out.println("Выберите сортировку: 1) Bubble  2) Parallel Merge  3) Even/Odd по id (на основе Parallel Merge)");
                    int s = InputUtil.readInt(SC, ">", 1, 3);

                    long t0 = System.currentTimeMillis();
                    if (s == 1) {
                        bubble.sort(arr, Comparator.naturalOrder());
                    } else if (s == 2) {
                        parallelMerge.sort(arr, Comparator.naturalOrder());
                    } else {
                        Function<Person, Integer> key = Person::getId;
                        EvenOddPartialSorter.sort(arr, key, Comparator.naturalOrder(), parallelMerge);
                    }
                    long t1 = System.currentTimeMillis();
                    System.out.println("Отсортировано за " + (t1 - t0) + " мс");

                    list = MyArrayList.fromArray(arr);
                }
                case 5 -> {
                    if (list.isEmpty()) {
                        System.out.println("Сначала заполните коллекцию.");
                        break;
                    }
                    Person[] arr = list.toArray(new Person[0]);
                    parallelMerge.sort(arr, Comparator.naturalOrder());
                    System.out.println("Введите данные искомого Person (точное совпадение):");
                    String name = InputUtil.readNonEmpty(SC, "Имя: ");
                    int age = InputUtil.readInt(SC, "Возраст (0..150): ", 0, 150);
                    int id = InputUtil.readInt(SC, "ID (>=0): ", 0, Integer.MAX_VALUE);

                    Person target = new Person.Builder().name(name).age(age).id(id).build();
                    int idx = BinarySearch.binarySearch(arr, target, Comparator.naturalOrder());

                    if (idx >= 0) {
                        System.out.println("Найдено на индексе: " + idx + " -> " + arr[idx]);
                        FileIO.appendLine(OUTPUT_FILE, "SEARCH: found at " + idx + " -> " + arr[idx]);
                    } else {
                        System.out.println("Не найдено.");
                        FileIO.appendLine(OUTPUT_FILE, "SEARCH: not found -> " + target);
                    }
                }
                case 6 -> {
                    if (list.isEmpty()) {
                        System.out.println("Сначала заполните коллекцию.");
                        break;
                    }
                    System.out.println("Введите Person для подсчёта вхождений:");
                    String name = InputUtil.readNonEmpty(SC, "Имя: ");
                    int age = InputUtil.readInt(SC, "Возраст (0..150): ", 0, 150);
                    int id = InputUtil.readInt(SC, "ID (>=0): ", 0, Integer.MAX_VALUE);
                    Person target = new Person.Builder().name(name).age(age).id(id).build();

                    Person[] arr = list.toArray(new Person[0]);
                    int threads = Math.max(2, Runtime.getRuntime().availableProcessors() / 2);
                    long t0 = System.currentTimeMillis();
                    long cnt = OccurrenceCounter.countOccurrencesParallel(arr, target, threads);
                    long t1 = System.currentTimeMillis();
                    System.out.println("Вхождений: " + cnt + " (за " + (t1 - t0) + " мс)");
                }
                case 7 -> {
                    if (list.isEmpty()) {
                        System.out.println("Сначала заполните коллекцию.");
                        break;
                    }
                    String line = "ARRAY: " + Arrays.toString(list.toArray(new Person[0]));
                    FileIO.appendLine(OUTPUT_FILE, line);
                    System.out.println("Сохранено в " + OUTPUT_FILE);
                }
                case 8 -> {
                    int n = InputUtil.readInt(SC, "Сколько первых элементов показать? ", 1, Math.max(1, list.size()));
                    for (int i = 0; i < Math.min(n, list.size()); i++) {
                        System.out.println(i + ": " + list.get(i));
                    }
                }
                case 9 -> {
                    if (list.isEmpty()) {
                        System.out.println("Сначала заполните коллекцию.");
                        break;
                    }
                    Person[] arr = list.toArray(new Person[0]);
                    Comparator<Person> byAge = Comparator.comparingInt(Person::getAge)
                            .thenComparingInt(Person::getId)
                            .thenComparing(Person::getName);
                    new ParallelMergeSortStrategy<Person>().sort(arr, byAge);
                    list = MyArrayList.fromArray(arr);
                    System.out.println("Отсортировано по age,id,name (Parallel Merge).");
                }
                case 10 -> {
                    int n = InputUtil.readInt(SC, "Новая длина: ", 1, 2000000);
                    list = DataFactory.randomPersonsStream(n);
                    System.out.println("Создано (Stream): " + list.size());
                }
                case 11 -> {
                    if (list.isEmpty()) {
                        System.out.println("Сначала заполните коллекцию.");
                        break;
                    }
                    Person[] arr = list.toArray(new Person[0]);
                    EvenOddPartialSorter.sort(arr, Person::getId, Comparator.naturalOrder(), parallelMerge);
                    list = MyArrayList.fromArray(arr);
                    System.out.println("Выполнена частичная сортировка: чётные id отсортированы, нечётные — на местах.");
                }
                case 12 -> {
                    list = new MyArrayList<>();
                    System.out.println("Коллекция очищена.");
                }
                default -> System.out.println("Неверный выбор.");
            }
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("=== МЕНЮ ===");
        System.out.println("1) Заполнить вручную");
        System.out.println("2) Заполнить случайно");
        System.out.println("3) Загрузить из файла");
        System.out.println("4) Сортировать (выбор алгоритма)");
        System.out.println("5) Бинарный поиск");
        System.out.println("6) Многопоточный подсчёт вхождений");
        System.out.println("7) Сохранить текущий массив в файл (append)");
        System.out.println("8) Показать первые N элементов");
        System.out.println("9) Сортировать по age,id,name (кастомный компаратор)");
        System.out.println("10) Заполнить случайно через Stream API");
        System.out.println("11) Частичная сортировка: чётные id сортируются, нечётные на местах");
        System.out.println("12) Очистить коллекцию");
        System.out.println("0) Выход");
    }
}


