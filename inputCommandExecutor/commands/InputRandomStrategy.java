package inputCommandExecutor.commands;

import data.Data;
import data.DataBuilder;
import inputCommandExecutor.InputStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Стратегия случайного заполнения данными
 */
public class InputRandomStrategy implements InputStrategy {
    private List<Data> dataList = new ArrayList<>();
    private Scanner scanner;
    private final String[] SAMPLE_DATA = {"A", "B", "C", "D", "E"};
    private final Random random = new Random();

    public InputRandomStrategy(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public List<Data> execute() {
        System.out.println("Введите колличество генерируемых элементов:");
        int userInput = scanner.nextInt();
        for (int i = 0; i < userInput; i++) {
            dataList.add(generateRandomData());
        }
        return dataList;
    }

    private Data generateRandomData() {
        String letter = SAMPLE_DATA[random.nextInt(SAMPLE_DATA.length)];
        int number = random.nextInt(100); // Случайное значение от 0 до 99
        boolean logical = random.nextBoolean(); // Случайное логическое значение

        return new DataBuilder()
                .setLetter(letter)
                .setNumber(number)
                .setLogical(logical)
                .build();
    }
}
