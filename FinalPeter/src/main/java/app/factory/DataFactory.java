package app.factory;

import app.collection.MyArrayList;
import app.model.Person;
import app.util.InputUtil;

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public final class DataFactory {
    private DataFactory() {}

    public static MyArrayList<Person> manualPersons(Scanner sc, int n) {
        MyArrayList<Person> list = new MyArrayList<>();
        for (int i = 0; i < n; i++) {
            System.out.println("[" + (i + 1) + "/" + n + "] Введите данные:");
            String name = InputUtil.readNonEmpty(sc, "Имя: ");
            int age = InputUtil.readInt(sc, "Возраст (0..150): ", 0, 150);
            int id = InputUtil.readInt(sc, "ID (>=0): ", 0, Integer.MAX_VALUE);
            Person p = new Person.Builder().name(name).age(age).id(id).build();
            list.add(p);
        }
        return list;
    }

    public static MyArrayList<Person> randomPersons(int n) {
        MyArrayList<Person> list = new MyArrayList<>();
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        for (int i = 0; i < n; i++) {
            String name = "User" + rnd.nextInt(1000000);
            int age = rnd.nextInt(0, 101);
            int id = rnd.nextInt(0, 1_000_000);
            Person p = new Person.Builder().name(name).age(age).id(id).build();
            list.add(p);
        }
        return list;
    }

    public static MyArrayList<Person> randomPersonsStream(int n) {
        MyArrayList<Person> list = new MyArrayList<>();
        IntStream.range(0, n).forEach(i -> {
            int id = ThreadLocalRandom.current().nextInt(0, 1_000_000);
            int age = ThreadLocalRandom.current().nextInt(0, 101);
            String name = "User" + id;
            Person p = new Person.Builder().name(name).age(age).id(id).build();
            list.add(p);
        });
        return list;
    }
}
