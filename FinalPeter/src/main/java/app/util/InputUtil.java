package app.util;

import java.util.Scanner;

public final class InputUtil {
    private InputUtil() {}

    public static int readInt(Scanner sc, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String token = sc.next();
            try {
                int v = Integer.parseInt(token);
                if (v < min || v > max) {
                    System.out.println("Допустимо: [" + min + ".." + max + "]");
                    continue;
                }
                return v;
            } catch (NumberFormatException e) {
                System.out.println("Введите целое число.");
            }
        }
    }

    public static String readNonEmpty(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.next();
            if (s != null && !s.trim().isEmpty()) return s.trim();
            System.out.println("Строка не должна быть пустой.");
        }
    }
}
