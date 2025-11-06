package app.util;

import app.collection.MyArrayList;
import app.model.Person;

import java.io.*;
import java.nio.charset.StandardCharsets;

public final class FileIO {
    private FileIO() {}

    public static MyArrayList<Person> readPersonsFromCsv(File file) {
        MyArrayList<Person> list = new MyArrayList<>();
        if (file == null || !file.exists()) {
            System.out.println("Файл не найден.");
            return list;
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            int lineNo = 0;
            while ((line = br.readLine()) != null) {
                lineNo++;
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("[;,	]");
                if (parts.length < 3) {
                    System.out.println("Пропуск строки " + lineNo + ": недостаточно полей.");
                    continue;
                }
                String name = parts[0].trim();
                Integer age = parseIntSafe(parts[1].trim());
                Integer id = parseIntSafe(parts[2].trim());
                if (name.isEmpty() || age == null || id == null) {
                    System.out.println("Пропуск строки " + lineNo + ": невалидные данные.");
                    continue;
                }
                try {
                    Person p = new Person.Builder().name(name).age(age).id(id).build();
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

    private static Integer parseIntSafe(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return null;
        }
    }

    public static void appendLine(String path, String line) {
        try (FileOutputStream fos = new FileOutputStream(path, true);
             OutputStreamWriter osw = new OutputStreamWriter(fos, java.nio.charset.StandardCharsets.UTF_8);
             BufferedWriter bw = new BufferedWriter(osw)) {
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Ошибка записи в файл: " + e.getMessage());
        }
    }
}
