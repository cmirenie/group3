package app.tests;

import app.collection.MyArrayList;
import app.concurrency.OccurrenceCounter;
import app.model.Person;
import app.search.BinarySearch;
import app.sort.BubbleSortStrategy;
import app.sort.EvenOddPartialSorter;
import app.sort.ParallelMergeSortStrategy;
import app.sort.SortStrategy;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;

public class ManualTests {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=== ManualTests start ===");

        testBuilderAndEquals();
        testBubbleSortNatural();
        testParallelMergeSortNatural();
        testEvenOddPartialSortById();
        testBinarySearchNatural();
        testOccurrenceCounter();
        testMyArrayListOperations();
        testCustomComparatorAgeIdName();

        System.out.println("\n=== ManualTests summary ===");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        if (failed == 0) {
            System.out.println("ALL TESTS PASSED");
        } else {
            System.out.println("SOME TESTS FAILED");
        }
    }

    private static void check(boolean condition, String message) {
        if (condition) {
            passed++;
            System.out.println("✓ " + message);
        } else {
            failed++;
            System.out.println("✗ " + message);
        }
    }

    private static <T> boolean isSorted(T[] data, Comparator<T> cmp) {
        for (int i = 1; i < data.length; i++) {
            if (cmp.compare(data[i - 1], data[i]) > 0) {
                return false;
            }
        }
        return true;
    }

    private static void testBuilderAndEquals() {
        Person a = new Person.Builder()
                .name("Alice")
                .age(30)
                .id(10)
                .build();

        Person b = new Person.Builder()
                .name("Alice")
                .age(30)
                .id(10)
                .build();

        Person c = new Person.Builder()
                .name("Bob")
                .age(25)
                .id(11)
                .build();

        check(a.equals(b), "Builder produces equal objects for same data");
        check(!a.equals(c), "equals() distinguishes different objects");
        check(a.hashCode() == b.hashCode(), "hashCode is consistent with equals");
    }

    private static Person[] baseArray() {
        return new Person[] {
                new Person.Builder().name("Bob").age(25).id(4).build(),
                new Person.Builder().name("Alice").age(30).id(9).build(),
                new Person.Builder().name("Carl").age(28).id(2).build(),
                new Person.Builder().name("Dima").age(22).id(7).build(),
                new Person.Builder().name("Eve").age(35).id(6).build()
        };
    }

    private static void testBubbleSortNatural() {
        Person[] arr = baseArray();
        SortStrategy<Person> bubble = new BubbleSortStrategy<>();
        bubble.sort(arr, Comparator.naturalOrder());

        check(isSorted(arr, Comparator.naturalOrder()), "BubbleSort: array is sorted (natural order)");
    }

    private static void testParallelMergeSortNatural() {
        Person[] arr = baseArray();
        SortStrategy<Person> merge = new ParallelMergeSortStrategy<>();
        merge.sort(arr, Comparator.naturalOrder());

        check(isSorted(arr, Comparator.naturalOrder()), "ParallelMergeSort: array is sorted (natural order)");
    }

    private static void testEvenOddPartialSortById() {
        Person[] original = baseArray();
        Person[] arr = Arrays.copyOf(original, original.length);

        Function<Person, Integer> key = Person::getId;
        SortStrategy<Person> inner = new ParallelMergeSortStrategy<>();
        EvenOddPartialSorter.sort(arr, key, Comparator.naturalOrder(), inner);

        boolean oddsStayed = true;
        for (int i = 0; i < original.length; i++) {
            if ((original[i].getId() & 1) == 1) {
                if (!arr[i].equals(original[i])) {
                    oddsStayed = false;
                    break;
                }
            }
        }
        check(oddsStayed, "EvenOdd: элементы с нечетным идентификатором остаются в исходных позициях");

        int evCount = 0;
        for (Person p : original) {
            if ((p.getId() & 1) == 0) evCount++;
        }
        Person[] evBefore = new Person[evCount];
        int idx = 0;
        for (Person p : original) {
            if ((p.getId() & 1) == 0) {
                evBefore[idx++] = p;
            }
        }
        new app.sort.ParallelMergeSortStrategy<app.model.Person>().sort(evBefore, Comparator.naturalOrder());
        Person[] evAfter = new Person[evCount];
        idx = 0;
        for (int i = 0; i < arr.length; i++) {
            if ((original[i].getId() & 1) == 0) {
                evAfter[idx++] = arr[i];
            }
        }
        boolean evensSorted = Arrays.equals(evBefore, evAfter);
        check(evensSorted, "EvenOdd: сортируется подпоследовательность четных идентификаторов");
    }

    private static void testBinarySearchNatural() {
        Person[] arr = baseArray();
        SortStrategy<Person> merge = new ParallelMergeSortStrategy<>();
        merge.sort(arr, Comparator.naturalOrder());

        Person target = new Person.Builder()
                .name("Carl")
                .age(28)
                .id(2)
                .build();

        int pos = BinarySearch.binarySearch(arr, target, Comparator.naturalOrder());
        check(pos >= 0, "BinarySearch: target found (natural order)");
        if (pos >= 0) {
            check(arr[pos].equals(target), "BinarySearch: found element equals target");
        }
    }

    private static void testOccurrenceCounter() {
        Person x = new Person.Builder().name("X").age(20).id(100).build();
        Person y = new Person.Builder().name("Y").age(21).id(101).build();

        Person[] data = new Person[] { x, y, x, x, y, x, y, x, x, y };
        long cnt = OccurrenceCounter.countOccurrencesParallel(data, x, 4);

        check(cnt == 6L, "OccurrenceCounter: parallel count equals expected (6)");
    }

    private static void testMyArrayListOperations() {
        MyArrayList<Person> list = new MyArrayList<>();

        Person a = new Person.Builder().name("A").age(10).id(1).build();
        Person b = new Person.Builder().name("B").age(11).id(2).build();
        Person c = new Person.Builder().name("C").age(12).id(3).build();

        list.add(a);
        list.add(b);
        list.add(c);

        check(list.get(0).equals(a), "MyArrayList.get works");
        check(list.get(1).equals(b), "MyArrayList.get works (index 1)");

        Person newB = new Person.Builder().name("B2").age(13).id(22).build();
        Person old = list.set(1, newB);
        check(old.equals(b) && list.get(1).equals(newB), "MyArrayList.set replaces value and returns old");

        Person removed = list.remove(2);
        check(removed.equals(c) && list.size() == 2, "MyArrayList.remove reduces size and returns removed element");

        Person[] arr = list.toArray(new Person[0]);
        check(arr.length == 2 && arr[0].equals(a) && arr[1].equals(newB), "MyArrayList.toArray(T[]) produces typed array");

        Object[] raw = list.toArray();
        check(raw.length == 2 && raw[0].equals(a) && raw[1].equals(newB), "MyArrayList.toArray() produces Object[]");
    }

    private static void testCustomComparatorAgeIdName() {
        Person[] arr = baseArray();
        Comparator<Person> byAgeIdName = Comparator
                .comparingInt(Person::getAge)
                .thenComparingInt(Person::getId)
                .thenComparing(Person::getName);

        SortStrategy<Person> merge = new ParallelMergeSortStrategy<>();
        merge.sort(arr, byAgeIdName);

        check(isSorted(arr, byAgeIdName), "ParallelMergeSort: custom comparator (age,id,name) works");
    }
}

