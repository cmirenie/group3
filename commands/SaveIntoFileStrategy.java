package collectionComandExecutor.commands;

import collectionComandExecutor.CollectionStrategy;
import data.Data;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Scanner;

/**
 * Дополнительное задание 2: реализовать функционал для записи отсортированных коллекций и
 * найденных значений в файл в режиме добавления данных.
 */
public record SaveIntoFileStrategy(Scanner scanner) implements CollectionStrategy {

    @Override
    public void execute(List<Data> input) {
        while (true) {
            System.out.println("Сохранить результаты в файл? (Д/Н)");
            String choice = scanner.nextLine();
            if (choice.compareTo("Н") == 0) {
                return;
            } else if (choice.compareTo("Д") == 0) {
                break;
            } else {
                System.out.println("Ошибка: данного варианта выбора не существует.");
            }
        }
        Path path = requestFilePathFromConsole();
        appendToFile(path, input);
    }

    // Добавление элементов в конец файла
    private void appendToFile(Path file, List<Data> input) {
        if (file == null) {
            System.err.println("Путь к файлу не задан.");
            return;
        }
        if (input == null || input.isEmpty()) {
            System.err.println("Коллекция данных пуста.");
            return;
        }
        try {
            Path parent = file.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            try (BufferedWriter bufferedWriter = Files.newBufferedWriter(
                    file,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND,
                    StandardOpenOption.WRITE)) {

                for (Data item : input) {
                    if (item == null) continue;
                    String letter = item.getLetter();
                    int number = item.getNumber();
                    boolean logical = item.isLogical();
                    bufferedWriter.write(letter + "," + number + "," + logical);
                    bufferedWriter.write(System.lineSeparator());
                }
            }
        } catch (Exception e) {
            System.err.println("Сбой записи в файл '" + file + "': " + e.getMessage());
        }
    }

    // Запрос пути к файлу

    private Path requestFilePathFromConsole() {
        while (true) {
            System.out.print("Введите путь к файлу для дозаписи: ");
            try {
                if (!scanner.hasNextLine()) {
                    System.err.println("Ввод недоступен.");
                    return null;
                }
                String trimmed = scanner.nextLine().trim();
                if (trimmed.isEmpty()) {
                    System.out.println("Путь пуст. Повторите ввод.");
                    continue;
                }
                Path path = Paths.get(expandTilde(trimmed));
                if (Files.exists(path) && Files.isDirectory(path)) {
                    System.out.println("Указан каталог. Нужен путь к файлу.");
                    continue;
                }
                return path;
            } catch (Exception e) {
                System.err.println("Ошибка чтения ввода: " + e.getMessage());
                return null;
            }
        }
    }

    private static String expandTilde(String path) {
        if (path.equals("~")) return System.getProperty("user.home");
        String separator = File.separator;
        if (path.startsWith("~" + separator)) {
            return System.getProperty("user.home") + path.substring(1);
        }
        return path;
    }
}
