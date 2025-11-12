package inputCommandExecutor.commands;

import inputCommandExecutor.InputStrategy;
import data.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/** Стратегия: случайное заполнение коллекции. */
public final class InputRandomStrategy implements InputStrategy {
    private static final String[] SAMPLE_DATA = {"A", "B", "C", "D", "E"};

    private final List<Data> dataList = new ArrayList<>();
    private final Scanner scanner;
    private final Random random = new Random();

    public InputRandomStrategy(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public List<Data> execute() {
        System.out.println("Введите количество генерируемых элементов (целое неотрицательное):");

        int count = readNonNegativeInt(scanner);
        for (int i = 0; i < count; i++) {
            dataList.add(generateRandomData());
        }
        return dataList;
    }

    private Data generateRandomData() {
        String letter = SAMPLE_DATA[random.nextInt(SAMPLE_DATA.length)];
        int number = random.nextInt(1_000);
        boolean logical = random.nextBoolean();
        return new Data(letter, number, logical);
    }

    private static int readNonNegativeInt(Scanner sc) {
        while (true) {
            if (sc.hasNextInt()) {
                int v = sc.nextInt();
                sc.nextLine(); // съедаем перевод строки
                if (v >= 0) return v;
            } else {
                sc.nextLine(); // очистка неверного токена
            }
            System.out.println("Ошибка. Введите целое неотрицательное число:");
        }
    }
}
