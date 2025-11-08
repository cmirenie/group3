package searchCommandExecutor.commands;

import data.Data;
import searchCommandExecutor.SearchStrategy;

import java.util.List;
/**
 * Стратегия бинарного поиска по числовому полю
 */
public class SearchByNumberStrategy implements SearchStrategy {

    @Override
    public List<Data> execute(List<Data> input) {
        return List.of();
    }
}
