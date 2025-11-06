
public class Data {

    private String letter;
    private int number;
    private boolean logical;

    public Data(String letter, int number, boolean logical) {
        this.letter = letter;
        this.number = number;
        this.logical = logical;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "Data: "
                + "слово= " + letter
                + ", число=" + number
                + ", логика=" + logical + "\n";
    }

}
