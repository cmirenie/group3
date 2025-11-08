package data;

public class DataBuilder {
    private String letter;
    private int number;
    private boolean logical;

    public DataBuilder setLetter(String letter) {
        this.letter = letter;
        return this;
    }

    public DataBuilder setNumber(int number) {
        this.number = number;
        return this;
    }

    public DataBuilder setLogical(boolean logical) {
        this.logical = logical;
        return this;
    }

    public Data build() {
        return new Data(letter, number, logical);
    }
}