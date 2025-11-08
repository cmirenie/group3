package searchCommandExecutor.commands;

import data.Data;
import searchCommandExecutor.SearchStrategy;

import java.util.List;

public class EmptySearchStrategy implements SearchStrategy {
    @Override
    public List<Data> execute(List<Data> input) {
        return List.of();
    }
}
