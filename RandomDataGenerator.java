import java.util.Random;

public class RandomDataGenerator {
    private static final String[] SAMPLE_DATA = {"A", "B", "C", "D", "E"};
    private static final Random random = new Random();

    public static Data generateRandomData() {
        String letter = SAMPLE_DATA[random.nextInt(SAMPLE_DATA.length)];
        int number = random.nextInt(100); // Случайное значение от 0 до 99
        boolean logical = random.nextBoolean(); // Случайное логическое значение

        return new DataBuilder()
                .setLetter(letter)
                .setNumber(number)
                .setLogical(logical)
                .build();
    }
}