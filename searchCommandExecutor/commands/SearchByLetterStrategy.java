package searchCommandExecutor.commands;

import data.Data;
import searchCommandExecutor.SearchStrategy;

import java.util.List;

/**
 * Стратегия бинарного поиска по текстовому полю
 */
public class SearchByLetterStrategy implements SearchStrategy {
    @Override
    public List<Data> execute(List<Data> input) {
        return List.of();
    }
}
