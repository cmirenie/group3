package app.util;

import app.collection.MyArrayList;
import app.model.Person;
import app.model.validators.PersonValidator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public final class FileIO {
    private FileIO() { }

    public static MyArrayList<Person> readPersonsFromCsv(File file) {
        MyArrayList<Person> list = new MyArrayList<>();
        if (file == null || !file.exists()) {
            System.out.println("Файл не найден.");
            return list;
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            String line;
            int lineNo = 0;

            while ((line = br.readLine()) != null) {
                lineNo++;
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split("[;,\t]");
                if (parts.length < 3) {
                    System.out.println("Пропуск строки " + lineNo + ": недостаточно полей.");
                    continue;
                }

                String name = parts[0].trim();
                Integer age = parseIntSafe(parts[1].trim());
                Integer id  = parseIntSafe(parts[2].trim());

                // Единая валидация "сырых" значений
                if (PersonValidator.isInvalidRaw(name, age, id)) {
                    System.out.println("Пропуск строки " + lineNo + ": невалидные данные.");
                    continue;
                }

                // Авто-распаковка после проверки (без .intValue())
                int ageVal = Objects.requireNonNull(age, "age");
                int idVal  = Objects.requireNonNull(id, "id");

                try {
                    Person p = new Person.Builder()
                            .name(name)
                            .age(ageVal)
                            .id(idVal)
                            .build();
                    list.add(p);
                } catch (IllegalArgumentException ex) {
                    System.out.println("Пропуск строки " + lineNo + ": " + ex.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        }

        return list;
    }

    public static void appendLine(String path, String line) {
        if (path == null || path.isEmpty()) {
            return;
        }
        try {
            File target = new File(path);
            File parent = target.getParentFile();
            if (parent != null && !parent.exists()) {
                boolean ok = parent.mkdirs();
                if (!ok && !parent.exists()) {
                    System.out.println("Не удалось создать каталог: " + parent.getAbsolutePath());
                }
            }

            try (FileOutputStream fos = new FileOutputStream(target, true);
                 OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                 BufferedWriter bw = new BufferedWriter(osw)) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Ошибка записи в файл: " + e.getMessage());
        }
    }

    private static Integer parseIntSafe(String s) {
        try {
            if (s == null) {
                return null;
            }
            return Integer.valueOf(s);
        } catch (Exception e) {
            return null;
        }
    }
}
