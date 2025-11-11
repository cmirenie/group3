package data;

import java.util.Comparator;
import java.util.List;

public class Data implements Comparable<Data> {

    private String letter;
    private int number;
    private boolean logical;


    public static void bubbleSort(List<Data> list, Comparator<Data> cmp) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (cmp.compare(list.get(j), list.get(j + 1)) > 0) {
                    Data temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
    }

    public Data(String letter, int number, boolean logical) {
        this.letter = letter;
        this.number = number;
        this.logical = logical;
    }
    public String getLetter() { return letter; }
    public boolean isLogical() { return logical; }
    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "data.Data: "
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