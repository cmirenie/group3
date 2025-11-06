import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Data> dataList = new ArrayList<>();

        System.out.println("Выберите способ ввода данных:\n1 - Ввести данные вручную\n2 - Сгенерировать случайные данные\n3 - загрузить из файла");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume the newline

        switch (choice) {
            case 1:
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
                break;

            case 2:
                for (int i = 0; i < 3; i++) { // Генерируем 3 случайных объекта
                    dataList.add(RandomDataGenerator.generateRandomData());
                }
                break;

            case 3:
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
                break;

            default:
                System.out.println("Неверный выбор.");
                break;
        }

        System.out.println("Сохраненные данные:");
        for (Data data : dataList) {
            System.out.println(data);
        }
    }
}