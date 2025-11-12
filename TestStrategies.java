import collectionComandExecutor.commands.SortByLetterStrategy;
import collectionComandExecutor.commands.SortByLogicalStrategy;
import collectionComandExecutor.commands.SortByNumberStrategy;
import collectionComandExecutor.commands.SortBySpecialStrategy;
import data.Data;
import data.DataBuilder;
import searchCommandExecutor.commands.BinarySearchStrategy;

// Базовый набор данных
private static Data[] baseArray() {
    return new Data[]{
            new DataBuilder().setLetter("C").setNumber(26).setLogical(true).build(),
            new DataBuilder().setLetter("A").setNumber(5).setLogical(false).build(),
            new DataBuilder().setLetter("B").setNumber(57).setLogical(true).build(),
            new DataBuilder().setLetter("E").setNumber(68).setLogical(false).build(),
            new DataBuilder().setLetter("D").setNumber(9).setLogical(true).build()
    };
}

// Сравнение списков по строковому представлению
private static boolean listsEqual(List<Data> l1, List<Data> l2) {
    if (l1.size() != l2.size()) return false;
    for (int i = 0; i < l1.size(); i++) {
        if (!l1.get(i).toString().equals(l2.get(i).toString())) return false;
    }
    return true;
}

private static List<Data> toList(Data... arr) {
    return new ArrayList<>(Arrays.asList(arr));
}

private static void testSortByLetter() {
    List<Data> list = toList(baseArray());
    new SortByLetterStrategy().execute(list);

    List<Data> expected = toList(
            new DataBuilder().setLetter("A").setNumber(5).setLogical(false).build(),
            new DataBuilder().setLetter("B").setNumber(57).setLogical(true).build(),
            new DataBuilder().setLetter("C").setNumber(26).setLogical(true).build(),
            new DataBuilder().setLetter("D").setNumber(9).setLogical(true).build(),
            new DataBuilder().setLetter("E").setNumber(68).setLogical(false).build()
    );

    IO.println("SortByLetter: " + (listsEqual(list, expected) ? "OK" : "FAIL"));
}

private static void testSortByNumber() {
    List<Data> list = toList(baseArray());
    new SortByNumberStrategy().execute(list);

    List<Data> expected = toList(
            new DataBuilder().setLetter("A").setNumber(5).setLogical(false).build(),
            new DataBuilder().setLetter("D").setNumber(9).setLogical(true).build(),
            new DataBuilder().setLetter("C").setNumber(26).setLogical(true).build(),
            new DataBuilder().setLetter("B").setNumber(57).setLogical(true).build(),
            new DataBuilder().setLetter("E").setNumber(68).setLogical(false).build()
    );

    IO.println("SortByNumber: " + (listsEqual(list, expected) ? "OK" : "FAIL"));
}

private static void testSortByLogical() {
    List<Data> list = toList(baseArray());
    new SortByLogicalStrategy().execute(list);

    // false → true (натуральный порядок Boolean)
    List<Data> expected = toList(
            new DataBuilder().setLetter("A").setNumber(5).setLogical(false).build(),
            new DataBuilder().setLetter("E").setNumber(68).setLogical(false).build(),
            new DataBuilder().setLetter("C").setNumber(26).setLogical(true).build(),
            new DataBuilder().setLetter("B").setNumber(57).setLogical(true).build(),
            new DataBuilder().setLetter("D").setNumber(9).setLogical(true).build()
    );

    IO.println("SortByLogical: " + (listsEqual(list, expected) ? "OK" : "FAIL"));
}

private static void testSortBySpecial() {
    // Чётные сортируем, нечётные остаются на своих местах
    List<Data> list = toList(
            new DataBuilder().setLetter("X").setNumber(8).setLogical(true).build(),   // чётный
            new DataBuilder().setLetter("Y").setNumber(5).setLogical(false).build(),  // НЕ чётный
            new DataBuilder().setLetter("Z").setNumber(2).setLogical(true).build(),   // чётный
            new DataBuilder().setLetter("W").setNumber(7).setLogical(true).build()    // НЕ чётный
    );
    new SortBySpecialStrategy().execute(list);

    List<Data> expected = toList(
            new DataBuilder().setLetter("Z").setNumber(2).setLogical(true).build(),
            new DataBuilder().setLetter("Y").setNumber(5).setLogical(false).build(),  // остался на месте
            new DataBuilder().setLetter("X").setNumber(8).setLogical(true).build(),
            new DataBuilder().setLetter("W").setNumber(7).setLogical(true).build()    // остался на месте
    );

    IO.println("SortBySpecial (even-only): " + (listsEqual(list, expected) ? "OK" : "FAIL"));
}

private static void testBinarySearchByLetter() throws NoSuchFieldException {
    List<Data> list = toList(baseArray());
    // Сначала сортируем по letter, затем ищем
    new SortByLetterStrategy().execute(list);

    Field f = Data.class.getDeclaredField("letter");
    // Подставляем «ввод» через Scanner по строке, Scanner наследовать нельзя (он final)
    Scanner fakeInput = new Scanner("C\n");
    BinarySearchStrategy search = new BinarySearchStrategy(f, fakeInput);

    List<Data> res = search.execute(list);
    boolean ok = !res.isEmpty() && "C".equals(res.getFirst().getLetter());
    IO.println("BinarySearch by letter: " + (ok ? "OK" : "FAIL"));
}

void main() throws Exception {
    testSortByLetter();
    testSortByNumber();
    testSortByLogical();
    testSortBySpecial();
    testBinarySearchByLetter();
}
