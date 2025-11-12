package inputCommandExecutor.commands;

import inputCommandExecutor.InputStrategy;
import data.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/** Стратегия: ручной ввод пользователем. */
public final class InputManualStrategy implements InputStrategy {
    private final List<Data> dataList = new ArrayList<>();
    private final Scanner scanner;

    public InputManualStrategy(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public List<Data> execute() {
        System.out.println("""
                Вводите строки формата:
                <слово>,<число>,<true/false>
                Примеры: A,10,true   или   test,5,false
                Пустая строка — закончить ввод.
                """.strip());

        while (true) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) break;

            String[] parts = line.split(",");
            if (parts.length != 3) {
                System.out.println("Ошибка: нужно три значения, разделённых запятыми. Повторите ввод.");
                continue;
            }

            String letter = parts[0].trim();
            Integer number = tryParseInt(parts[1].trim());
            Boolean logical = tryParseBoolean(parts[2].trim());

            if (letter.isEmpty() || number == null || logical == null) {
                System.out.println("Ошибка разбора. Проверьте формат и повторите ввод.");
                continue;
            }

            dataList.add(new Data(letter, number, logical));
        }

        return dataList;
    }

    private static Integer tryParseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    // Поддержка true/false, 1/0, да/нет
    private static Boolean tryParseBoolean(String s) {
        String v = s.toLowerCase(Locale.ROOT);
        if (v.equals("true") || v.equals("1") || v.equals("да")) return true;
        if (v.equals("false") || v.equals("0") || v.equals("нет")) return false;
        return null;
    }
}
