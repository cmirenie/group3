
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class DataApp {

    private static void saveToFile(List<Data> dataList, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Data data : dataList) {
                writer.write(data.toString());
            }
            System.out.println("Результат сохранен в файл: " + filename);
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении файла: " + e.getMessage());
        }
    }

    public static void bubbleSort(List<Data> list, Comparator<Data> cmp) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (cmp.compare(list.get(j), list.get(j + 1)) > 0) {
                    Data temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
    }

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
        // 2.1 Сортировка по первому полю - letter
        bubbleSort(dataList, Comparator.comparing(Data::getLetter));
        saveToFile(dataList, "sort_by_letter.txt");

        // 2.2 Сортировка по второму полю - number
        bubbleSort(dataList, Comparator.comparingInt(Data::getNumber));
        saveToFile(dataList, "sort_by_number.txt");

        // 2.3 Сортировка по третьему полю - logical
        bubbleSort(dataList, Comparator.comparing(Data::getLogical));
        saveToFile(dataList, "sort_by_logical.txt");

        // 2.4 Особая сортировка (ваша уже реализованная)
        bubbleSort(dataList, (d1, d2) -> {
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
            }
        });
        saveToFile(dataList, "special_sort.txt");

        sortEvenNumbers(dataList);
        System.out.println("Сохраненные данные:");
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
