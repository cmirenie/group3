package collectionComandExecutor.commands;

import collectionComandExecutor.CollectionStrategy;
import data.Data;

import java.util.Comparator;
import java.util.List;
/**
 * Стратегия сортировки по текстовому полю
 */

public class SortByLetterStrategy implements CollectionStrategy {
    public static void BubbleSortByLetter(List<Data> list) {
    Data.bubbleSort(list, Comparator.comparing(Data::getLetter));
    }
    @Override
    public void execute(List<Data> input) {

    }


}
