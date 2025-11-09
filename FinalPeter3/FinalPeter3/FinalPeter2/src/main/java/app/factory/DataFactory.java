package app.factory;

import app.collection.MyArrayList;
import app.model.Person;
import app.model.validators.PersonValidator;
import app.util.InputUtil;

import java.util.Random;
import java.util.Scanner;
import java.util.stream.IntStream;

public final class DataFactory {
    private DataFactory() { }

    public static MyArrayList<Person> manualPersons(Scanner sc, int count) {
        MyArrayList<Person> list = new MyArrayList<>();
        for (int i = 0; i < count; i++) {
            System.out.println();
            System.out.println("— Ввод элемента " + (i + 1) + " из " + count);

            String name = InputUtil.readNonEmpty(sc, "Имя: ");
            int age = InputUtil.readInt(sc, "Возраст (0..150): ", 0, 150);
            int id  = InputUtil.readInt(sc, "ID (>=0): ", 0, Integer.MAX_VALUE);

            if (PersonValidator.isInvalidRaw(name, age, id)) {
                System.out.println("Данные не прошли валидацию, повторите ввод.");
                i--;
                continue;
            }

            Person p = new Person.Builder()
                    .name(name)
                    .age(age)
                    .id(id)
                    .build();

            list.add(p);
        }
        return list;
    }

    /** Случайные данные (без Stream API). */
    public static MyArrayList<Person> randomPersons(int count) {
        Random rnd = new Random();
        MyArrayList<Person> list = new MyArrayList<>();
        for (int i = 0; i < count; i++) {
            String name = "User" + (1000 + rnd.nextInt(9000));
            int age = rnd.nextInt(151);          // 0..150
            int id  = rnd.nextInt(Integer.MAX_VALUE);

            // теоретически валидно по нашему правилу, но оставим явную проверку
            if (PersonValidator.isInvalidRaw(name, age, id)) {
                i--;
                continue;
            }

            Person p = new Person.Builder()
                    .name(name)
                    .age(age)
                    .id(id)
                    .build();

            list.add(p);
        }
        return list;
    }

    public static MyArrayList<Person> randomPersonsStream(int count) {
        Random rnd = new Random();
        MyArrayList<Person> list = new MyArrayList<>();
        IntStream.range(0, count).forEach(i -> {
            String name = "User" + (1000 + rnd.nextInt(9000));
            int age = rnd.nextInt(151);
            int id  = rnd.nextInt(Integer.MAX_VALUE);

            if (PersonValidator.isInvalidRaw(name, age, id)) {
                return; // пропускаем элемент
            }

            Person p = new Person.Builder()
                    .name(name)
                    .age(age)
                    .id(id)
                    .build();

            list.add(p);
        });
        return list;
    }
}
