public class Data {
    private String letter; // String
    private int number;    // int
    private boolean logical; // boolean

    public Data(String letter, int number, boolean logical) {
        this.letter = letter;
        this.number = number;
        this.logical = logical;
    }

    @Override
    public String toString() {
        return "Data: " +
                "слово= " + letter +
                ", число=" + number +
                ", логика=" + logical + "\n";
    }
}