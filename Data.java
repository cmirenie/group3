
public class Data implements Comparable<Data> {

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

    @Override
    public int compareTo(Data other) {
        int cmp = this.letter.compareTo(other.letter);
        if (cmp != 0) {
            return cmp;
        }

        cmp = Integer.compare(this.number, other.number);
        if (cmp != 0) {
            return cmp;
        }

        return Boolean.compare(this.logical, other.logical);
    }

}
