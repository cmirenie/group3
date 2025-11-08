package inputCommandExecutor.commands;

import data.Data;
import data.DataBuilder;
import inputCommandExecutor.InputStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Стратегия ручного ввода данных
 */
public class InputManualStrategy implements InputStrategy {
    private List<Data> dataList = new ArrayList<>();
    private Scanner scanner;

    public InputManualStrategy(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public List<Data> execute() {
        while (true) {
            System.out.println("Введите данные (слово, число, true/false):");
            String userInput = scanner.nextLine();
            String[] fields = userInput.split(",");
            if (fields.length == 3) {
                try {
                    String letter = fields[0].trim();
                    int number = Integer.parseInt(fields[1].trim());
                    boolean logical = Boolean.parseBoolean(fields[2].trim());

                    Data userData = new DataBuilder()
                            .setLetter(letter)
                            .setNumber(number)
                            .setLogical(logical)
                            .build();

                    dataList.add(userData);
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка: поле 2 должно быть целым числом.");
                }
            } else {
                System.out.println("Ошибка: необходимо ввести 3 поля.");
            }
            while (true) {
                System.out.println("Продолжить ввод? (Д/Н)");
                userInput = scanner.nextLine();
                if (userInput.compareTo("Н") == 0) {
                    return dataList;
                } else if (userInput.compareTo("Д") != 0) {
                    System.out.println("Ошибка: данного варианта выбора не существует.");
                }
            }
        }
    }
}
