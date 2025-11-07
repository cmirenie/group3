package inputCommandExecutor.commands;

import data.Data;
import data.DataBuilder;
import inputCommandExecutor.InputStrategy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/**
 * Стратегия чтения данных из файла
 */
public class InputFromFileStrategy implements InputStrategy {
    private List<Data> dataList = new ArrayList<>();
    private Scanner scanner;

    public InputFromFileStrategy(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public List<Data> execute() {
        System.out.println("Введите путь к файлу:");
        String filePath = scanner.nextLine();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fileFields = line.split(",");
                if (fileFields.length == 3) {
                    try {
                        String field1 = fileFields[0].trim();
                        int field2 = Integer.parseInt(fileFields[1].trim());
                        boolean field3 = Boolean.parseBoolean(fileFields[2].trim());

                        Data fileData = new DataBuilder()
                                .setLetter(field1)
                                .setNumber(field2)
                                .setLogical(field3)
                                .build();
                        dataList.add(fileData);
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка при чтении файла: поле 2 должно быть целым числом.");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        }
        return dataList;
    }
}
