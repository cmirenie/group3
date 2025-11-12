package inputCommandExecutor.commands;

import inputCommandExecutor.InputStrategy;
import data.Data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/** Стратегия: чтение данных из текстового файла построчно. */
public final class InputFromFileStrategy implements InputStrategy {
    private final List<Data> dataList = new ArrayList<>();
    private final Scanner scanner;

    public InputFromFileStrategy(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public List<Data> execute() {
        System.out.println("Введите путь к файлу (формат строк: <слово>,<число>,<true/false>):");
        String path = scanner.nextLine().trim();

        int ok = 0, bad = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) continue;

                String[] parts = trimmed.split(",");
                if (parts.length != 3) {
                    bad++;
                    continue;
                }

                String letter = parts[0].trim();
                Integer number = tryParseInt(parts[1].trim());
                Boolean logical = tryParseBoolean(parts[2].trim());

                if (letter.isEmpty() || number == null || logical == null) {
                    bad++;
                    continue;
                }

                dataList.add(new Data(letter, number, logical));
                ok++;
            }
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
            // Возвращаем то, что успели прочитать (может быть пустым)
            return dataList;
        }

        System.out.printf("Готово. Корректных строк: %d, пропущено: %d%n", ok, bad);
        return dataList;
    }

    private static Integer tryParseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private static Boolean tryParseBoolean(String s) {
        String v = s.toLowerCase(Locale.ROOT);
        if (v.equals("true") || v.equals("1") || v.equals("да")) return true;
        if (v.equals("false") || v.equals("0") || v.equals("нет")) return false;
        return null;
    }
}
