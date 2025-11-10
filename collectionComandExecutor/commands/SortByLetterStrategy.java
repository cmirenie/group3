package collectionComandExecutor.commands;

import collectionComandExecutor.CollectionStrategy;
import data.Data;

import java.util.Comparator;
import java.util.List;
/**
 * Стратегия сортировки по текстовому полю
 */

public class SortByLetterStrategy implements CollectionStrategy {
    @Override
    public void execute(List<Data> input) {
        Data.bubbleSort(input, Comparator.comparing(Data::getLetter));
    }


}
