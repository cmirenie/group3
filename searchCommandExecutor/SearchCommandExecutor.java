package searchCommandExecutor;

import data.Data;

import java.util.List;

public class SearchCommandExecutor {
    private SearchStrategy strategy;

    public SearchCommandExecutor(SearchStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(SearchStrategy strategy) {
        this.strategy = strategy;
    }

    public List<Data> executeCommand(List<Data> input) {
        return strategy.execute(input);
    }
}